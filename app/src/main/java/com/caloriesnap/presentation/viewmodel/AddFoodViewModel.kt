package com.caloriesnap.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caloriesnap.data.repository.FoodRepository
import com.caloriesnap.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AddFoodState(
    val searchQuery: String = "",
    val searchResults: List<Food> = emptyList(),
    val favorites: List<Food> = emptyList(),
    val recent: List<Food> = emptyList(),
    val saved: Boolean = false
)

@HiltViewModel
class AddFoodViewModel @Inject constructor(private val repository: FoodRepository) : ViewModel() {
    private val _state = MutableStateFlow(AddFoodState())
    val state: StateFlow<AddFoodState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFavoriteFoods().collect { _state.update { s -> s.copy(favorites = it) } }
        }
        viewModelScope.launch {
            repository.getRecentFoods().collect { _state.update { s -> s.copy(recent = it) } }
        }
    }

    fun search(query: String) {
        _state.update { it.copy(searchQuery = query) }
        if (query.length >= 1) {
            viewModelScope.launch {
                val results = repository.searchFoods(query)
                _state.update { it.copy(searchResults = results) }
            }
        } else {
            _state.update { it.copy(searchResults = emptyList()) }
        }
    }

    fun addFood(food: Food, weight: Float, mealType: MealType) {
        viewModelScope.launch {
            val ratio = weight / 100f
            repository.addRecord(FoodRecord(
                date = LocalDate.now().toString(),
                mealType = mealType,
                foodName = food.name,
                weight = weight,
                calories = food.calories * ratio,
                protein = food.protein * ratio,
                carbs = food.carbs * ratio,
                fat = food.fat * ratio
            ))
            repository.addToRecent(food.id)
            _state.update { it.copy(saved = true) }
        }
    }

    fun addCustomFood(name: String, calories: Float, protein: Float, carbs: Float, fat: Float, weight: Float, mealType: MealType) {
        viewModelScope.launch {
            val food = Food(name = name, calories = calories, protein = protein, carbs = carbs, fat = fat)
            repository.addCustomFood(food)
            val ratio = weight / 100f
            repository.addRecord(FoodRecord(
                date = LocalDate.now().toString(),
                mealType = mealType,
                foodName = name,
                weight = weight,
                calories = calories * ratio,
                protein = protein * ratio,
                carbs = carbs * ratio,
                fat = fat * ratio
            ))
            _state.update { it.copy(saved = true) }
        }
    }

    fun toggleFavorite(food: Food) {
        viewModelScope.launch { repository.setFavorite(food.id, !_state.value.favorites.any { it.id == food.id }) }
    }

    fun resetSaved() { _state.update { it.copy(saved = false) } }
}
