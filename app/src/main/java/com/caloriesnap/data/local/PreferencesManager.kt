package com.caloriesnap.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.caloriesnap.domain.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {
    private object Keys {
        val GENDER = stringPreferencesKey("gender")
        val AGE = intPreferencesKey("age")
        val HEIGHT = floatPreferencesKey("height")
        val WEIGHT = floatPreferencesKey("weight")
        val ACTIVITY = stringPreferencesKey("activity")
        val GOAL = stringPreferencesKey("goal")
        val CALORIE_ADJ = intPreferencesKey("calorie_adj")
        val AI_ENABLED = booleanPreferencesKey("ai_enabled")
        val API_URL = stringPreferencesKey("api_url")
        val API_KEY = stringPreferencesKey("api_key")
        val MODEL = stringPreferencesKey("model")
        val TIMEOUT = intPreferencesKey("timeout")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    enum class ThemeMode { SYSTEM, LIGHT, DARK }

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { p ->
        ThemeMode.valueOf(p[Keys.THEME_MODE] ?: "SYSTEM")
    }

    suspend fun saveThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    val userProfile: Flow<UserProfile> = context.dataStore.data.map { p ->
        UserProfile(
            gender = Gender.valueOf(p[Keys.GENDER] ?: "MALE"),
            age = p[Keys.AGE] ?: 25,
            height = p[Keys.HEIGHT] ?: 170f,
            weight = p[Keys.WEIGHT] ?: 65f,
            activityLevel = ActivityLevel.valueOf(p[Keys.ACTIVITY] ?: "MODERATE"),
            goal = Goal.valueOf(p[Keys.GOAL] ?: "MAINTAIN"),
            calorieAdjustment = p[Keys.CALORIE_ADJ] ?: 500
        )
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        context.dataStore.edit { p ->
            p[Keys.GENDER] = profile.gender.name
            p[Keys.AGE] = profile.age
            p[Keys.HEIGHT] = profile.height
            p[Keys.WEIGHT] = profile.weight
            p[Keys.ACTIVITY] = profile.activityLevel.name
            p[Keys.GOAL] = profile.goal.name
            p[Keys.CALORIE_ADJ] = profile.calorieAdjustment
        }
    }

    data class ApiConfig(
        val enabled: Boolean = false,
        val baseUrl: String = "https://api.openai.com/v1",
        val apiKey: String = "",
        val model: String = "gpt-4o",
        val timeout: Int = 30
    )

    val apiConfig: Flow<ApiConfig> = context.dataStore.data.map { p ->
        ApiConfig(
            enabled = p[Keys.AI_ENABLED] ?: false,
            baseUrl = p[Keys.API_URL] ?: "https://api.openai.com/v1",
            apiKey = p[Keys.API_KEY] ?: "",
            model = p[Keys.MODEL] ?: "gpt-4o",
            timeout = p[Keys.TIMEOUT] ?: 30
        )
    }

    suspend fun saveApiConfig(config: ApiConfig) {
        context.dataStore.edit { p ->
            p[Keys.AI_ENABLED] = config.enabled
            p[Keys.API_URL] = config.baseUrl
            p[Keys.API_KEY] = config.apiKey
            p[Keys.MODEL] = config.model
            p[Keys.TIMEOUT] = config.timeout
        }
    }
}
