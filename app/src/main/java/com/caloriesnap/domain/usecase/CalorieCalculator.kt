package com.caloriesnap.domain.usecase

import com.caloriesnap.domain.model.*
import javax.inject.Inject

class CalorieCalculator @Inject constructor() {
    fun calculateBMR(profile: UserProfile): Float {
        return if (profile.gender == Gender.MALE)
            10 * profile.weight + 6.25f * profile.height - 5 * profile.age + 5
        else 10 * profile.weight + 6.25f * profile.height - 5 * profile.age - 161
    }

    fun calculateTDEE(profile: UserProfile): Float = calculateBMR(profile) * profile.activityLevel.factor

    fun calculateTargetCalories(profile: UserProfile): Float {
        val tdee = calculateTDEE(profile)
        return when (profile.goal) {
            Goal.LOSE -> tdee - profile.calorieAdjustment
            Goal.MAINTAIN -> tdee
            Goal.GAIN -> tdee + profile.calorieAdjustment
        }
    }

    fun calculateDailySummary(records: List<FoodRecord>, profile: UserProfile): DailySummary {
        return DailySummary(
            totalCalories = records.sumOf { it.calories.toDouble() }.toFloat(),
            totalProtein = records.sumOf { it.protein.toDouble() }.toFloat(),
            totalCarbs = records.sumOf { it.carbs.toDouble() }.toFloat(),
            totalFat = records.sumOf { it.fat.toDouble() }.toFloat(),
            targetCalories = calculateTargetCalories(profile),
            records = records
        )
    }
}
