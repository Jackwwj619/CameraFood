package com.caloriesnap.domain.model

data class UserProfile(
    val gender: Gender = Gender.MALE,
    val age: Int = 25,
    val height: Float = 170f,
    val weight: Float = 65f,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val goal: Goal = Goal.MAINTAIN,
    val calorieAdjustment: Int = 500
)

enum class Gender { MALE, FEMALE }
enum class ActivityLevel(val factor: Float) {
    SEDENTARY(1.2f), LIGHT(1.375f), MODERATE(1.55f), HEAVY(1.725f), ATHLETE(1.9f)
}
enum class Goal { LOSE, MAINTAIN, GAIN }
enum class MealType { BREAKFAST, LUNCH, DINNER, SNACK }

data class Food(
    val id: Long = 0,
    val name: String,
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val per100g: Boolean = true
)

data class FoodRecord(
    val id: Long = 0,
    val date: String,
    val mealType: MealType,
    val foodName: String,
    val weight: Float,
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val imageUri: String? = null
)

data class WeightRecord(val id: Long = 0, val date: String, val weight: Float)

data class DailySummary(
    val totalCalories: Float,
    val totalProtein: Float,
    val totalCarbs: Float,
    val totalFat: Float,
    val targetCalories: Float,
    val records: List<FoodRecord>
)

data class AiRecognitionResult(
    val foods: List<RecognizedFood>,
    val totalCalories: Float,
    val description: String,
    val suggestion: String? = null
)

data class RecognizedFood(
    val name: String,
    val weight: Float,
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val confidence: Float
)
