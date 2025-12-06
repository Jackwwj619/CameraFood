package com.caloriesnap.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caloriesnap.data.local.DefaultFoods
import com.caloriesnap.data.local.PreferencesManager
import com.caloriesnap.data.repository.FoodRepository
import com.caloriesnap.domain.model.FoodRecord
import com.caloriesnap.domain.usecase.CalorieCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeState(
    val records: List<FoodRecord> = emptyList(),
    val totalCalories: Float = 0f,
    val totalProtein: Float = 0f,
    val totalCarbs: Float = 0f,
    val totalFat: Float = 0f,
    val targetCalories: Float = 2000f
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FoodRepository,
    private val prefs: PreferencesManager,
    private val calculator: CalorieCalculator
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            if (repository.getFoodCount() == 0) repository.insertDefaultFoods(DefaultFoods.list)
        }
        viewModelScope.launch {
            combine(
                repository.getRecordsByDate(LocalDate.now().toString()),
                prefs.userProfile
            ) { records, profile ->
                val summary = calculator.calculateDailySummary(records, profile)
                HomeState(records, summary.totalCalories, summary.totalProtein, summary.totalCarbs, summary.totalFat, summary.targetCalories)
            }.collect { _state.value = it }
        }
    }

    fun deleteRecord(id: Long) = viewModelScope.launch { repository.deleteRecord(id) }
}
