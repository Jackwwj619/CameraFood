package com.caloriesnap.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caloriesnap.data.local.PreferencesManager
import com.caloriesnap.data.remote.AiService
import com.caloriesnap.data.repository.FoodRepository
import com.caloriesnap.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class RecognitionState(
    val isLoading: Boolean = false,
    val foods: List<RecognizedFood> = emptyList(),
    val suggestion: String? = null,
    val error: String? = null,
    val aiEnabled: Boolean = false,
    val saved: Boolean = false
)

@HiltViewModel
class RecognitionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val aiService: AiService,
    private val repository: FoodRepository,
    private val prefs: PreferencesManager
) : ViewModel() {
    private val _state = MutableStateFlow(RecognitionState())
    val state: StateFlow<RecognitionState> = _state.asStateFlow()
    private var mealType = MealType.BREAKFAST
    private var imageUri: String = ""

    init {
        viewModelScope.launch {
            prefs.apiConfig.collect { _state.update { s -> s.copy(aiEnabled = it.enabled && it.apiKey.isNotBlank()) } }
        }
    }

    fun setMealType(type: MealType) { mealType = type }

    fun recognize(uriString: String) {
        imageUri = uriString
        if (!_state.value.aiEnabled) {
            _state.update { it.copy(error = "请先在设置中配置AI API") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val bitmap = loadBitmap(Uri.parse(java.net.URLDecoder.decode(uriString, "UTF-8")))
                if (bitmap == null) {
                    _state.update { it.copy(isLoading = false, error = "无法加载图片") }
                    return@launch
                }
                val profile = prefs.userProfile.first()
                val todayRecords = repository.getRecordsByDate(LocalDate.now().toString()).first()
                val consumed = todayRecords.sumOf { it.calories.toDouble() }.toFloat()

                aiService.recognizeFood(bitmap, profile, consumed).fold(
                    onSuccess = { result -> _state.update { it.copy(isLoading = false, foods = result.foods, suggestion = result.suggestion) } },
                    onFailure = { e -> _state.update { it.copy(isLoading = false, error = e.message) } }
                )
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun updateFood(index: Int, food: RecognizedFood) {
        _state.update { it.copy(foods = it.foods.toMutableList().apply { set(index, food) }) }
    }

    fun removeFood(index: Int) {
        _state.update { it.copy(foods = it.foods.toMutableList().apply { removeAt(index) }) }
    }

    fun saveRecords() {
        viewModelScope.launch {
            val date = LocalDate.now().toString()
            _state.value.foods.forEach { food ->
                repository.addRecord(FoodRecord(
                    date = date, mealType = mealType, foodName = food.name,
                    weight = food.weight, calories = food.calories,
                    protein = food.protein, carbs = food.carbs, fat = food.fat,
                    imageUri = imageUri
                ))
            }
            _state.update { it.copy(saved = true) }
        }
    }

    private fun loadBitmap(uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
        } catch (_: Exception) { null }
    }
}
