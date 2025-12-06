package com.caloriesnap.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caloriesnap.data.local.PreferencesManager
import com.caloriesnap.data.repository.FoodRepository
import com.caloriesnap.domain.model.*
import com.caloriesnap.domain.usecase.CalorieCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class ProfileState(
    val profile: UserProfile = UserProfile(),
    val bmr: Float = 0f,
    val tdee: Float = 0f,
    val targetCalories: Float = 0f,
    val weightRecords: List<WeightRecord> = emptyList()
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefs: PreferencesManager,
    private val repository: FoodRepository,
    private val calculator: CalorieCalculator
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            prefs.userProfile.collect { profile ->
                _state.update {
                    it.copy(
                        profile = profile,
                        bmr = calculator.calculateBMR(profile),
                        tdee = calculator.calculateTDEE(profile),
                        targetCalories = calculator.calculateTargetCalories(profile)
                    )
                }
            }
        }
        viewModelScope.launch {
            repository.getWeightRecords().collect { _state.update { s -> s.copy(weightRecords = it) } }
        }
    }

    fun updateProfile(profile: UserProfile) = viewModelScope.launch { prefs.saveUserProfile(profile) }

    fun addWeightRecord(weight: Float) = viewModelScope.launch {
        repository.addWeightRecord(LocalDate.now().toString(), weight)
        updateProfile(_state.value.profile.copy(weight = weight))
    }
}
