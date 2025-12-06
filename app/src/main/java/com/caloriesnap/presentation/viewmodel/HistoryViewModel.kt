package com.caloriesnap.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caloriesnap.data.repository.FoodRepository
import com.caloriesnap.domain.model.FoodRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HistoryState(
    val selectedDate: LocalDate = LocalDate.now(),
    val records: List<FoodRecord> = emptyList(),
    val totalCalories: Float = 0f
)

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repository: FoodRepository) : ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init { loadRecords(LocalDate.now()) }

    fun selectDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
        loadRecords(date)
    }

    private fun loadRecords(date: LocalDate) {
        viewModelScope.launch {
            repository.getRecordsByDate(date.toString()).collect { records ->
                _state.update { it.copy(records = records, totalCalories = records.sumOf { r -> r.calories.toDouble() }.toFloat()) }
            }
        }
    }

    fun deleteRecord(id: Long) = viewModelScope.launch { repository.deleteRecord(id) }
}
