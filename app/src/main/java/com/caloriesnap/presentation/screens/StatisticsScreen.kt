package com.caloriesnap.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.caloriesnap.presentation.viewmodel.StatisticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(topBar = { TopAppBar(title = { Text("统计") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            TabRow(selectedTab) {
                Tab(selectedTab == 0, onClick = { selectedTab = 0 }) { Text("周统计", Modifier.padding(12.dp)) }
                Tab(selectedTab == 1, onClick = { selectedTab = 1 }) { Text("月统计", Modifier.padding(12.dp)) }
            }
            Spacer(Modifier.height(16.dp))

            if (selectedTab == 0) {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("本周热量摄入", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(16.dp))
                        WeeklyChart(state.weeklyCalories, state.targetCalories)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("平均: ${state.weeklyAverage.toInt()} kcal")
                            Text("目标: ${state.targetCalories.toInt()} kcal")
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Card(Modifier.weight(1f)) {
                        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("达标天数", style = MaterialTheme.typography.bodySmall)
                            Text("${state.achievedDays}/7", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    Card(Modifier.weight(1f)) {
                        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("完成率", style = MaterialTheme.typography.bodySmall)
                            Text("${(state.achievedDays * 100 / 7)}%", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("本月热量趋势", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(16.dp))
                        WeeklyChart(state.monthlyCalories, state.targetCalories)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("日均: ${state.monthlyAverage.toInt()} kcal")
                            Text("体重变化: ${if (state.weightChange >= 0) "+" else ""}${String.format("%.1f", state.weightChange)} kg")
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Card(Modifier.weight(1f)) {
                        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("达标天数", style = MaterialTheme.typography.bodySmall)
                            Text("${state.monthlyAchievedDays}/30", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    Card(Modifier.weight(1f)) {
                        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("完成率", style = MaterialTheme.typography.bodySmall)
                            Text("${(state.monthlyAchievedDays * 100 / 30)}%", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("营养素分布", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))
                    NutrientPieChart(state.totalProtein, state.totalCarbs, state.totalFat)
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        NutrientLegend("蛋白质", state.totalProtein, Color(0xFF4CAF50))
                        NutrientLegend("碳水", state.totalCarbs, Color(0xFF2196F3))
                        NutrientLegend("脂肪", state.totalFat, Color(0xFFFF9800))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            if (state.topFoods.isNotEmpty()) {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("常吃食物 TOP5", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        state.topFoods.forEachIndexed { index, (name, count) ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("${index + 1}. $name")
                                Text("${count}次", color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("AI周报", style = MaterialTheme.typography.titleMedium)
                        Button(onClick = { viewModel.generateAiReport() }, enabled = !state.isLoadingReport) {
                            if (state.isLoadingReport) CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp) else Text("生成")
                        }
                    }
                    state.aiReport?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyChart(data: List<Pair<String, Float>>, target: Float) {
    val maxValue = maxOf(data.maxOfOrNull { it.second } ?: 0f, target) * 1.1f
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error

    Canvas(Modifier.fillMaxWidth().height(150.dp)) {
        val barWidth = size.width / data.size * 0.6f
        val spacing = size.width / data.size

        data.forEachIndexed { index, (_, value) ->
            val barHeight = if (maxValue > 0) (value / maxValue) * size.height else 0f
            val x = index * spacing + (spacing - barWidth) / 2
            val color = if (value > target * 1.1f) errorColor else primaryColor
            drawRect(color, Offset(x, size.height - barHeight), Size(barWidth, barHeight))
        }

        val targetY = size.height - (target / maxValue) * size.height
        drawLine(errorColor.copy(alpha = 0.5f), Offset(0f, targetY), Offset(size.width, targetY), strokeWidth = 2f)
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        data.forEach { (date, _) -> Text(date, style = MaterialTheme.typography.labelSmall) }
    }
}

@Composable
fun NutrientPieChart(protein: Float, carbs: Float, fat: Float) {
    val total = protein + carbs + fat
    if (total == 0f) return

    Canvas(Modifier.size(120.dp).padding(8.dp)) {
        var startAngle = -90f
        listOf(protein to Color(0xFF4CAF50), carbs to Color(0xFF2196F3), fat to Color(0xFFFF9800)).forEach { (value, color) ->
            val sweep = (value / total) * 360f
            drawArc(color, startAngle, sweep, useCenter = true, size = Size(size.width, size.height))
            startAngle += sweep
        }
    }
}

@Composable
fun NutrientLegend(name: String, value: Float, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(Modifier.size(12.dp)) { drawCircle(color) }
        Spacer(Modifier.width(4.dp))
        Text("$name ${value.toInt()}g", style = MaterialTheme.typography.bodySmall)
    }
}
