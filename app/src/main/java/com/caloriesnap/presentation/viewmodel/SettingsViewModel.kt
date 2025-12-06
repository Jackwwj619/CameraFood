package com.caloriesnap.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caloriesnap.data.local.PreferencesManager
import com.caloriesnap.data.remote.AiService
import com.caloriesnap.data.repository.FoodRepository
import com.caloriesnap.domain.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class SettingsState(
    val apiConfig: PreferencesManager.ApiConfig = PreferencesManager.ApiConfig(),
    val testResult: String? = null,
    val isTesting: Boolean = false,
    val exportResult: String? = null,
    val themeMode: PreferencesManager.ThemeMode = PreferencesManager.ThemeMode.SYSTEM
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefs: PreferencesManager,
    private val aiService: AiService,
    private val repository: FoodRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()
    private val gson = Gson()

    init {
        viewModelScope.launch { prefs.apiConfig.collect { _state.update { s -> s.copy(apiConfig = it) } } }
        viewModelScope.launch { prefs.themeMode.collect { _state.update { s -> s.copy(themeMode = it) } } }
    }

    fun updateApiConfig(config: PreferencesManager.ApiConfig) = viewModelScope.launch { prefs.saveApiConfig(config) }

    fun updateThemeMode(mode: PreferencesManager.ThemeMode) = viewModelScope.launch { prefs.saveThemeMode(mode) }

    fun testConnection() {
        viewModelScope.launch {
            _state.update { it.copy(isTesting = true, testResult = null) }
            aiService.testConnection().fold(
                onSuccess = { _state.update { s -> s.copy(isTesting = false, testResult = "✓ 连接成功") } },
                onFailure = { e -> _state.update { s -> s.copy(isTesting = false, testResult = "✗ ${e.message}") } }
            )
        }
    }

    fun exportData() {
        viewModelScope.launch {
            try {
                val profile = prefs.userProfile.first()
                val records = repository.getAllRecords()
                val weights = repository.getAllWeightRecords()
                val customFoods = repository.getCustomFoods()

                val export = ExportData(
                    version = "1.0",
                    exportDate = java.time.Instant.now().toString(),
                    userProfile = mapOf(
                        "gender" to profile.gender.name.lowercase(),
                        "age" to profile.age,
                        "height" to profile.height,
                        "weight" to profile.weight,
                        "activityLevel" to profile.activityLevel.name.lowercase(),
                        "goal" to profile.goal.name.lowercase(),
                        "calorieAdjustment" to profile.calorieAdjustment
                    ),
                    foodRecords = records.map { mapOf("date" to it.date, "meal" to it.mealType.name.lowercase(), "name" to it.foodName, "weight" to it.weight, "calories" to it.calories, "protein" to it.protein, "carbs" to it.carbs, "fat" to it.fat) },
                    weightRecords = weights.map { mapOf("date" to it.date, "weight" to it.weight) },
                    customFoods = customFoods.map { mapOf("name" to it.name, "calories" to it.calories, "protein" to it.protein, "carbs" to it.carbs, "fat" to it.fat) }
                )

                val file = File(context.getExternalFilesDir(null), "caloriesnap_export_${System.currentTimeMillis()}.json")
                file.writeText(gson.toJson(export))
                _state.update { it.copy(exportResult = "导出成功: ${file.absolutePath}") }
            } catch (e: Exception) {
                _state.update { it.copy(exportResult = "导出失败: ${e.message}") }
            }
        }
    }

    fun importData(json: String) {
        viewModelScope.launch {
            try {
                val data = gson.fromJson(json, ExportData::class.java)
                val profile = data.userProfile
                prefs.saveUserProfile(UserProfile(
                    gender = Gender.valueOf((profile["gender"] as String).uppercase()),
                    age = (profile["age"] as Number).toInt(),
                    height = (profile["height"] as Number).toFloat(),
                    weight = (profile["weight"] as Number).toFloat(),
                    activityLevel = ActivityLevel.valueOf((profile["activityLevel"] as String).uppercase()),
                    goal = Goal.valueOf((profile["goal"] as String).uppercase()),
                    calorieAdjustment = (profile["calorieAdjustment"] as Number).toInt()
                ))
                val records = data.foodRecords.map {
                    FoodRecord(date = it["date"] as String, mealType = MealType.valueOf((it["meal"] as String).uppercase()),
                        foodName = it["name"] as String, weight = (it["weight"] as Number).toFloat(),
                        calories = (it["calories"] as Number).toFloat(), protein = (it["protein"] as Number).toFloat(),
                        carbs = (it["carbs"] as Number).toFloat(), fat = (it["fat"] as Number).toFloat())
                }
                repository.importRecords(records)
                val weights = data.weightRecords.map { WeightRecord(date = it["date"] as String, weight = (it["weight"] as Number).toFloat()) }
                repository.importWeightRecords(weights)
                _state.update { it.copy(exportResult = "导入成功") }
            } catch (e: Exception) {
                _state.update { it.copy(exportResult = "导入失败: ${e.message}") }
            }
        }
    }

    fun clearMessage() { _state.update { it.copy(testResult = null, exportResult = null) } }

    private data class ExportData(val version: String, val exportDate: String, val userProfile: Map<String, Any>, val foodRecords: List<Map<String, Any>>, val weightRecords: List<Map<String, Any>>, val customFoods: List<Map<String, Any>>)
}
