package com.caloriesnap.data.remote

import android.graphics.Bitmap
import android.util.Base64
import com.caloriesnap.data.local.PreferencesManager
import com.caloriesnap.domain.model.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiService @Inject constructor(private val prefs: PreferencesManager) {
    private val gson = Gson()

    suspend fun recognizeFood(bitmap: Bitmap, profile: UserProfile, dailyConsumed: Float): Result<AiRecognitionResult> = withContext(Dispatchers.IO) {
        try {
            val config = prefs.apiConfig.first()
            if (!config.enabled || config.apiKey.isBlank()) return@withContext Result.failure(Exception("AI未启用"))

            val base64 = bitmap.toBase64()
            val targetCalories = calculateTargetCalories(profile)
            val remaining = targetCalories - dailyConsumed

            val prompt = buildPrompt(profile.goal, dailyConsumed, targetCalories, remaining)
            val request = buildRequest(config, base64, prompt)

            val client = OkHttpClient.Builder().callTimeout(config.timeout.toLong(), TimeUnit.SECONDS).build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) return@withContext Result.failure(Exception("API错误: ${response.code}"))

            val body = response.body?.string() ?: return@withContext Result.failure(Exception("空响应"))
            parseResponse(body)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun buildPrompt(goal: Goal, consumed: Float, target: Float, remaining: Float): String {
        val goalText = when (goal) { Goal.LOSE -> "减脂"; Goal.MAINTAIN -> "维持"; Goal.GAIN -> "增肌" }
        return """你是专业营养师AI。分析图片中的食物并估算热量。
用户目标：$goalText | 今日已摄入：${consumed.toInt()}kcal | 目标：${target.toInt()}kcal | 剩余：${remaining.toInt()}kcal

请返回JSON格式：
{"foods":[{"name":"食物名","weight_g":克数,"calories":热量,"protein":蛋白质g,"carbs":碳水g,"fat":脂肪g,"confidence":0.9}],"total_calories":总热量,"meal_description":"描述","suggestion":"针对用户目标的建议"}"""
    }

    private fun buildRequest(config: PreferencesManager.ApiConfig, base64: String, prompt: String): Request {
        val messages = listOf(mapOf("role" to "user", "content" to listOf(
            mapOf("type" to "text", "text" to prompt),
            mapOf("type" to "image_url", "image_url" to mapOf("url" to "data:image/jpeg;base64,$base64"))
        )))
        val body = gson.toJson(mapOf("model" to config.model, "messages" to messages, "max_tokens" to 1024))
        return Request.Builder()
            .url("${config.baseUrl}/chat/completions")
            .addHeader("Authorization", "Bearer ${config.apiKey}")
            .addHeader("Content-Type", "application/json")
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()
    }

    private fun parseResponse(body: String): Result<AiRecognitionResult> {
        return try {
            val json = gson.fromJson(body, ApiResponse::class.java)
            val content = json.choices.firstOrNull()?.message?.content ?: return Result.failure(Exception("无内容"))
            val cleaned = content.replace("```json", "").replace("```", "").trim()
            val result = gson.fromJson(cleaned, AiResult::class.java)
            Result.success(AiRecognitionResult(
                foods = result.foods.map { RecognizedFood(it.name, it.weight_g, it.calories, it.protein, it.carbs, it.fat, it.confidence) },
                totalCalories = result.total_calories,
                description = result.meal_description,
                suggestion = result.suggestion
            ))
        } catch (e: Exception) { Result.failure(e) }
    }

    private fun Bitmap.toBase64(): String {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
    }

    private fun calculateTargetCalories(profile: UserProfile): Float {
        val bmr = if (profile.gender == Gender.MALE)
            10 * profile.weight + 6.25f * profile.height - 5 * profile.age + 5
        else 10 * profile.weight + 6.25f * profile.height - 5 * profile.age - 161
        val tdee = bmr * profile.activityLevel.factor
        return when (profile.goal) {
            Goal.LOSE -> tdee - profile.calorieAdjustment
            Goal.MAINTAIN -> tdee
            Goal.GAIN -> tdee + profile.calorieAdjustment
        }
    }

    suspend fun testConnection(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val config = prefs.apiConfig.first()
            val body = gson.toJson(mapOf("model" to config.model, "messages" to listOf(mapOf("role" to "user", "content" to "Hi")), "max_tokens" to 5))
            val request = Request.Builder().url("${config.baseUrl}/chat/completions")
                .addHeader("Authorization", "Bearer ${config.apiKey}")
                .post(body.toRequestBody("application/json".toMediaType())).build()
            val response = OkHttpClient.Builder().callTimeout(10, TimeUnit.SECONDS).build().newCall(request).execute()
            if (response.isSuccessful) Result.success("连接成功") else Result.failure(Exception("错误: ${response.code}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun generateReport(prompt: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val config = prefs.apiConfig.first()
            if (!config.enabled || config.apiKey.isBlank()) return@withContext Result.failure(Exception("AI未启用"))
            val body = gson.toJson(mapOf("model" to config.model, "messages" to listOf(mapOf("role" to "user", "content" to prompt)), "max_tokens" to 1024))
            val request = Request.Builder().url("${config.baseUrl}/chat/completions")
                .addHeader("Authorization", "Bearer ${config.apiKey}")
                .addHeader("Content-Type", "application/json")
                .post(body.toRequestBody("application/json".toMediaType())).build()
            val response = OkHttpClient.Builder().callTimeout(config.timeout.toLong(), TimeUnit.SECONDS).build().newCall(request).execute()
            if (!response.isSuccessful) return@withContext Result.failure(Exception("API错误: ${response.code}"))
            val json = gson.fromJson(response.body?.string(), ApiResponse::class.java)
            Result.success(json.choices.firstOrNull()?.message?.content ?: "无内容")
        } catch (e: Exception) { Result.failure(e) }
    }

    private data class ApiResponse(val choices: List<Choice>)
    private data class Choice(val message: Message)
    private data class Message(val content: String)
    private data class AiResult(val foods: List<AiFood>, val total_calories: Float, val meal_description: String, val suggestion: String?)
    private data class AiFood(val name: String, val weight_g: Float, val calories: Float, val protein: Float, val carbs: Float, val fat: Float, val confidence: Float)
}
