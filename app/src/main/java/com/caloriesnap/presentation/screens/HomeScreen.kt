package com.caloriesnap.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.caloriesnap.domain.model.MealType
import com.caloriesnap.presentation.components.*
import com.caloriesnap.presentation.navigation.Screen
import com.caloriesnap.presentation.theme.*
import com.caloriesnap.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showMealDialog by remember { mutableStateOf(false) }

    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(
                    AppleGreen.copy(alpha = 0.15f),
                    AppleTeal.copy(alpha = 0.1f),
                    MaterialTheme.colorScheme.background
                )
            )
        )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showMealDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) { Icon(Icons.Default.Add, "添加", tint = Color.White) }
            }
        ) { padding ->
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
            // 顶部标题栏
            item {
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("今日饮食", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, "设置", tint = MaterialTheme.colorScheme.outline)
                    }
                }
            }
            // 热量卡片
            item {
                CalorieCard(state.totalCalories, state.targetCalories, state.totalProtein, state.totalCarbs, state.totalFat)
                Spacer(Modifier.height(12.dp))
            }
            // 运动记录入口
            item {
                GlassCard(Modifier.fillMaxWidth().clickable { navController.navigate(Screen.Exercise.route) }) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FitnessCenter, "运动", tint = AppleOrange, modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("运动记录", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text("记录今日运动消耗", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                            }
                        }
                        Icon(Icons.Default.ChevronRight, "进入", tint = MaterialTheme.colorScheme.outline)
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
            // 餐次列表
            MealType.entries.forEach { meal ->
                val mealRecords = state.records.filter { it.mealType == meal }
                val mealName = when (meal) { MealType.BREAKFAST -> "早餐"; MealType.LUNCH -> "午餐"; MealType.DINNER -> "晚餐"; MealType.SNACK -> "加餐" }
                val mealIcon = when (meal) { MealType.BREAKFAST -> Icons.Default.WbSunny; MealType.LUNCH -> Icons.Default.LightMode; MealType.DINNER -> Icons.Default.DarkMode; MealType.SNACK -> Icons.Default.Cookie }
                item {
                    MealCard(mealName, mealIcon, mealRecords.sumOf { it.calories.toDouble() }.toFloat(), mealRecords) {
                        navController.navigate(Screen.AddFood.createRoute(meal.name))
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
        }
    }

    if (showMealDialog) {
        AlertDialog(
            onDismissRequest = { showMealDialog = false },
            title = { Text("选择餐次") },
            text = {
                Column {
                    listOf("早餐" to MealType.BREAKFAST, "午餐" to MealType.LUNCH, "晚餐" to MealType.DINNER, "加餐" to MealType.SNACK).forEach { (name, type) ->
                        TextButton(onClick = { showMealDialog = false; navController.navigate(Screen.AddFood.createRoute(type.name)) }, Modifier.fillMaxWidth()) {
                            Text(name)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
fun CalorieCard(consumed: Float, target: Float, protein: Float, carbs: Float, fat: Float) {
    val progress = if (target > 0) (consumed / target).coerceIn(0f, 1.5f) else 0f
    val remaining = (target - consumed).coerceAtLeast(0f)

    GlassCard(Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("今日摄入", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            Box(contentAlignment = Alignment.Center) {
                AnimatedCircularProgress(progress = progress)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedNumber(targetValue = consumed.toInt())
                    Text("/ ${target.toInt()} kcal", style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("剩余 ${remaining.toInt()} kcal", color = if (remaining > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                NutrientInfo("蛋白质", protein, ProteinColor)
                NutrientInfo("碳水", carbs, CarbsColor)
                NutrientInfo("脂肪", fat, FatColor)
            }
        }
    }
}

@Composable
fun NutrientInfo(name: String, value: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(name, style = MaterialTheme.typography.bodySmall)
        Text("${value.toInt()}g", fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun MealCard(name: String, icon: androidx.compose.ui.graphics.vector.ImageVector, calories: Float, records: List<com.caloriesnap.domain.model.FoodRecord>, onAdd: () -> Unit) {
    GlassCard(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, name, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("${calories.toInt()} kcal", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                }
            }
            IconButton(onClick = onAdd) {
                Icon(Icons.Default.AddCircle, "添加", tint = MaterialTheme.colorScheme.primary)
            }
        }
        if (records.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            records.forEach { record ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(record.foodName, style = MaterialTheme.typography.bodyMedium)
                    Text("${record.calories.toInt()} kcal", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@Composable
fun FoodItem(name: String, weight: Float, calories: Float, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(vertical = 2.dp), shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Medium)
                Text("${weight.toInt()}g", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
            Text("${calories.toInt()} kcal", fontWeight = FontWeight.Bold)
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "删除", tint = MaterialTheme.colorScheme.error) }
        }
    }
}
