package com.caloriesnap.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.caloriesnap.domain.model.MealType
import com.caloriesnap.presentation.navigation.Screen
import com.caloriesnap.presentation.viewmodel.HistoryViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, viewModel: HistoryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val dates = remember { (0..13).map { LocalDate.now().minusDays(it.toLong()) } }

    Scaffold(topBar = { TopAppBar(title = { Text("饮食记录") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyRow(Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(dates) { date ->
                    val isSelected = date == state.selectedDate
                    Card(
                        modifier = Modifier.clickable { viewModel.selectDate(date) },
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                    ) {
                        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(date.format(DateTimeFormatter.ofPattern("MM/dd")), color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
                            Text(when { date == LocalDate.now() -> "今天"; date == LocalDate.now().minusDays(1) -> "昨天"; else -> date.dayOfWeek.name.take(3) },
                                style = MaterialTheme.typography.bodySmall, color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }

            Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("总热量", style = MaterialTheme.typography.titleMedium)
                    Text("${state.totalCalories.toInt()} kcal", fontWeight = FontWeight.Bold)
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
