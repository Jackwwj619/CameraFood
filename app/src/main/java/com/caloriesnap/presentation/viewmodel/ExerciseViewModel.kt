package com.caloriesnap.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caloriesnap.data.local.dao.ExerciseRecordDao
import com.caloriesnap.data.local.entity.ExerciseRecordEntity
import com.caloriesnap.domain.model.ExerciseRecord
import com.caloriesnap.domain.model.ExerciseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class ExerciseState(
    val selectedDate: LocalDate = LocalDate.now(),
    val records: List<ExerciseRecord> = emptyList(),
    val totalCaloriesBurned: Float = 0f
)

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseRecordDao: ExerciseRecordDao
) : ViewModel() {
    private val _state = MutableStateFlow(ExerciseState())
    val state: StateFlow<ExerciseState> = _state.asStateFlow()

    init { loadRecords() }

    private fun loadRecords() {
        viewModelScope.launch {
            exerciseRecordDao.getByDate(_state.value.selectedDate.toString()).collect { entities ->
                val records = entities.map { ExerciseRecord(it.id, it.date, it.exerciseName, it.duration, it.caloriesBurned) }
                _state.update { it.copy(records = records, totalCaloriesBurned = records.sumOf { r -> r.caloriesBurned.toDouble() }.toFloat()) }
            }
        }
    }

    fun selectDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
        loadRecords()
    }

    fun addExercise(type: ExerciseType, duration: Int) {
        viewModelScope.launch {
            val calories = type.caloriesPerMinute * duration
            exerciseRecordDao.insert(ExerciseRecordEntity(
                date = _state.value.selectedDate.toString(),
                exerciseName = type.displayName,
                duration = duration,
                caloriesBurned = calories
            ))
        }
    }

    fun addCustomExercise(name: String, duration: Int, calories: Float) {
        viewModelScope.launch {
            exerciseRecordDao.insert(ExerciseRecordEntity(
                date = _state.value.selectedDate.toString(),
                exerciseName = name,
                duration = duration,
                caloriesBurned = calories
            ))
        }
    }

    fun deleteRecord(id: Long) {
        viewModelScope.launch { exerciseRecordDao.deleteById(id) }
    }
}
