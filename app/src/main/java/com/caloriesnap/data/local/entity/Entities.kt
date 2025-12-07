package com.caloriesnap.data.local.entity

import androidx.room.*

@Entity(tableName = "food_records")
data class FoodRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val mealType: String,
    val foodName: String,
    val weight: Float,
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val imageUri: String? = null
)

@Entity(tableName = "weight_records")
data class WeightRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val weight: Float
)

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val isCustom: Boolean = false,
    val isFavorite: Boolean = false
)

@Entity(tableName = "recent_foods")
data class RecentFoodEntity(
    @PrimaryKey val foodId: Long,
    val usedAt: Long
)

@Entity(tableName = "exercise_records")
data class ExerciseRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val exerciseName: String,
    val duration: Int,
    val caloriesBurned: Float
)
