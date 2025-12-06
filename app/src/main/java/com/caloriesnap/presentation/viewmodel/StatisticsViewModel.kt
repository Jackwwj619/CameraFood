package com.caloriesnap.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caloriesnap.data.local.PreferencesManager
import com.caloriesnap.data.remote.AiService
import com.caloriesnap.data.repository.FoodRepository
import com.caloriesnap.domain.model.*
import com.caloriesnap.domain.usecase.CalorieCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class StatisticsState(
    val weeklyCalories: List<Pair<String, Float>> = emptyList(),
    val weeklyAverage: Float = 0f,
    val targetCalories: Float = 2000f,
    val achievedDays: Int = 0,
    val totalProtein: Float = 0f,
    val totalCarbs: Float = 0f,
    val totalFat: Float = 0f,
    val weightRecords: List<WeightRecord> = emptyList(),
    val topFoods: List<Pair<String, Int>> = emptyList(),
    val aiReport: String? = null,
    val isLoadingReport: Boolean = false,
    val monthlyCalories: List<Pair<String, Float>> = emptyList(),
    val monthlyAverage: Float = 0f,
    val monthlyAchievedDays: Int = 0,
    val weightChange: Float = 0f
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: FoodRepository,
    private val prefs: PreferencesManager,
    private val calculator: CalorieCalculator,
    private val aiService: AiService
) : ViewModel() {
    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    init { loadData() }

    private fun loadData() {
        val endDate = LocalDate.now()
        val weekStart = endDate.minusDays(6)
        val monthStart = endDate.minusDays(29)

        viewModelScope.launch {
            combine(
                repository.getRecordsByDateRange(monthStart.toString(), endDate.toString()),
                repository.getWeightByDateRange(monthStart.toString(), endDate.toString()),
                prefs.userProfile
            ) { records, weights, profile ->
                val target = calculator.calculateTargetCalories(profile)
                val weeklyRecords = records.filter { it.date >= weekStart.toString() }
                val dailyCalories = (0..6).map { i ->
                    val date = weekStart.plusDays(i.toLong())
                    val dayRecords = weeklyRecords.filter { it.date == date.toString() }
                    date.toString().takeLast(5) to dayRecords.sumOf { it.calories.toDouble() }.toFloat()
                }
                val achieved = dailyCalories.count { it.second in (target * 0.9f)..(target * 1.1f) }
                val foodCounts = records.groupingBy { it.foodName }.eachCount().entries.sortedByDescending { it.value }.take(5)

                val monthlyCalories = (0..3).map { w ->
                    val wStart = monthStart.plusDays((w * 7).toLong())
                    val wEnd = wStart.plusDays(6)
                    val wRecords = records.filter { it.date >= wStart.toString() && it.date <= wEnd.toString() }
                    "第${w + 1}周" to wRecords.sumOf { it.calories.toDouble() }.toFloat() / 7
                }
                val monthlyAchieved = (0..29).count { i ->
                    val d = monthStart.plusDays(i.toLong()).toString()
                    val cal = records.filter { it.date == d }.sumOf { it.calories.toDouble() }.toFloat()
                    cal in (target * 0.9f)..(target * 1.1f)
                }
                val weightChange = if (weights.size >= 2) weights.last().weight - weights.first().weight else 0f

                StatisticsState(
                    weeklyCalories = dailyCalories,
                    weeklyAverage = dailyCalories.map { it.second }.average().toFloat(),
                    targetCalories = target,
                    achievedDays = achieved,
                    totalProtein = weeklyRecords.sumOf { it.protein.toDouble() }.toFloat(),
                    totalCarbs = weeklyRecords.sumOf { it.carbs.toDouble() }.toFloat(),
                    totalFat = weeklyRecords.sumOf { it.fat.toDouble() }.toFloat(),
                    weightRecords = weights,
                    topFoods = foodCounts.map { it.key to it.value },
                    monthlyCalories = monthlyCalories,
                    monthlyAverage = records.sumOf { it.calories.toDouble() }.toFloat() / 30,
                    monthlyAchievedDays = monthlyAchieved,
                    weightChange = weightChange
                )
            }.collect { _state.value = it }
        }
    }

    fun generateAiReport() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingReport = true) }
            val s = _state.value
            val profile = prefs.userProfile.first()
            val goalText = when (profile.goal) { Goal.LOSE -> "减脂"; Goal.MAINTAIN -> "维持"; Goal.GAIN -> "增肌" }
            val prompt = """用户目标：$goalText
本周数据摘要：
- 平均每日摄入：${s.weeklyAverage.toInt()} kcal（目标：${s.targetCalories.toInt()} kcal）
- 目标达成天数：${s.achievedDays}/7
- 营养素：蛋白质${s.totalProtein.toInt()}g、碳水${s.totalCarbs.toInt()}g、脂肪${s.totalFat.toInt()}g
- 体重变化：${s.weightChange} kg
- 高频食物：${s.topFoods.joinToString { it.first }}

请分析本周饮食情况并给出：1.本周饮食总结 2.针对${goalText}目标的改进建议 3.下周饮食计划建议"""

            aiService.generateReport(prompt).onSuccess { report ->
                _state.update { it.copy(aiReport = report, isLoadingReport = false) }
            }.onFailure {
                _state.update { it.copy(aiReport = "生成失败: ${it.aiReport}", isLoadingReport = false) }
            }
        }
    }
}
