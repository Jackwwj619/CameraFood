package com.caloriesnap.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.caloriesnap.presentation.components.GlassCard
import com.caloriesnap.presentation.navigation.Screen
import com.caloriesnap.presentation.theme.*
import com.caloriesnap.presentation.viewmodel.HistoryViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, viewModel: HistoryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.selectedDate.toEpochDay() * 86400000L
    )

    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(AppleBlue.copy(alpha = 0.12f), AppleTeal.copy(alpha = 0.08f), MaterialTheme.colorScheme.background)
            )
        )
    ) {
        Scaffold(containerColor = Color.Transparent) { padding ->
            Column(Modifier.fillMaxSize().padding(padding)) {
                Text("饮食记录", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
            // 日期选择器
            GlassCard(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable { showDatePicker = true }
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            when {
                                state.selectedDate == LocalDate.now() -> "今天"
                                state.selectedDate == LocalDate.now().minusDays(1) -> "昨天"
                                else -> state.selectedDate.format(DateTimeFormatter.ofPattern("yyyy年M月d日"))
                            },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            state.selectedDate.format(DateTimeFormatter.ofPattern("EEEE")),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    Row {
                        IconButton(onClick = { viewModel.selectDate(state.selectedDate.minusDays(1)) }) {
                            Icon(Icons.Default.ChevronLeft, "前一天")
                        }
                        IconButton(onClick = { viewModel.selectDate(state.selectedDate.plusDays(1)) }) {
                            Icon(Icons.Default.ChevronRight, "后一天")
                        }
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarMonth, "选择日期")
                        }
                    }
                }
            }

            // 热量统计
            Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(12.dp)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("总热量", style = MaterialTheme.typography.titleMedium)
                    Text("${state.totalCalories.toInt()} kcal", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }

            LazyColumn(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                MealType.entries.forEach { meal ->
                    val mealRecords = state.records.filter { it.mealType == meal }
                    if (mealRecords.isNotEmpty()) {
                        item {
                            Text(when (meal) { MealType.BREAKFAST -> "早餐"; MealType.LUNCH -> "午餐"; MealType.DINNER -> "晚餐"; MealType.SNACK -> "加餐" },
                                style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
                        }
                        items(mealRecords) { record ->
                            FoodItem(record.foodName, record.weight, record.calories) { viewModel.deleteRecord(record.id) }
                        }
                    }
                }
                if (state.records.isEmpty()) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Restaurant, "无记录", Modifier.size(48.dp), tint = MaterialTheme.colorScheme.outline)
                                Spacer(Modifier.height(8.dp))
                                Text("暂无记录", color = MaterialTheme.colorScheme.outline)
                                if (state.selectedDate == LocalDate.now()) {
                                    Spacer(Modifier.height(16.dp))
                                    Button(onClick = { navController.navigate(Screen.AddFood.createRoute("BREAKFAST")) }) { Text("添加食物") }
                                }
                            }
                        }
                    }
                }
            }
        }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.selectDate(LocalDate.ofEpochDay(it / 86400000L))
                    }
                    showDatePicker = false
                }) { Text("确定") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("取消") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
