package com.caloriesnap.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.caloriesnap.domain.model.ExerciseType
import com.caloriesnap.presentation.components.GlassCard
import com.caloriesnap.presentation.theme.*
import com.caloriesnap.presentation.viewmodel.ExerciseViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(navController: NavController, viewModel: ExerciseViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = state.selectedDate.toEpochDay() * 86400000L)

    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(colors = listOf(AppleOrange.copy(alpha = 0.12f), AppleGreen.copy(alpha = 0.08f), MaterialTheme.colorScheme.background))
        )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(onClick = { showAddDialog = true }, containerColor = MaterialTheme.colorScheme.primary) {
                    Icon(Icons.Default.Add, "添加运动", tint = Color.White)
                }
            }
        ) { padding ->
            LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp)) {
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("运动记录", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.Close, "关闭", tint = MaterialTheme.colorScheme.outline)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    GlassCard(Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(
                                    when { state.selectedDate == LocalDate.now() -> "今天"; state.selectedDate == LocalDate.now().minusDays(1) -> "昨天"; else -> state.selectedDate.format(DateTimeFormatter.ofPattern("yyyy年M月d日")) },
                                    style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold
                                )
                                Text(state.selectedDate.format(DateTimeFormatter.ofPattern("EEEE")), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                            }
                            Row {
                                IconButton(onClick = { viewModel.selectDate(state.selectedDate.minusDays(1)) }) { Icon(Icons.Default.ChevronLeft, "前一天") }
                                IconButton(onClick = { viewModel.selectDate(state.selectedDate.plusDays(1)) }) { Icon(Icons.Default.ChevronRight, "后一天") }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                item {
                    GlassCard(Modifier.fillMaxWidth()) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("消耗热量", style = MaterialTheme.typography.titleMedium)
                                Text("${state.totalCaloriesBurned.toInt()} kcal", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = AppleOrange)
                            }
                            Icon(Icons.Default.LocalFireDepartment, "热量", tint = AppleOrange, modifier = Modifier.size(48.dp))
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                if (state.records.isEmpty()) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.FitnessCenter, "无记录", Modifier.size(48.dp), tint = MaterialTheme.colorScheme.outline)
                                Spacer(Modifier.height(8.dp))
                                Text("暂无运动记录", color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    }
                } else {
                    items(state.records) { record ->
                        GlassCard(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Text(record.exerciseName, fontWeight = FontWeight.Medium)
                                    Text("${record.duration} 分钟", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("-${record.caloriesBurned.toInt()} kcal", fontWeight = FontWeight.Bold, color = AppleOrange)
                                    IconButton(onClick = { viewModel.deleteRecord(record.id) }) {
                                        Icon(Icons.Default.Delete, "删除", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddExerciseDialog(onDismiss = { showAddDialog = false }, onAdd = { type, duration -> viewModel.addExercise(type, duration); showAddDialog = false })
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = { TextButton(onClick = { datePickerState.selectedDateMillis?.let { viewModel.selectDate(LocalDate.ofEpochDay(it / 86400000L)) }; showDatePicker = false }) { Text("确定") } },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("取消") } }
        ) { DatePicker(state = datePickerState) }
    }
}

@Composable
fun AddExerciseDialog(onDismiss: () -> Unit, onAdd: (ExerciseType, Int) -> Unit) {
    var selectedType by remember { mutableStateOf(ExerciseType.RUNNING) }
    var duration by remember { mutableStateOf("30") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加运动") },
        text = {
            Column {
                Text("选择运动类型", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(200.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ExerciseType.entries.toList()) { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type.displayName, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("时长(分钟)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                val calories = duration.toIntOrNull()?.let { it * selectedType.caloriesPerMinute } ?: 0f
                Text("预计消耗: ${calories.toInt()} kcal", style = MaterialTheme.typography.bodyMedium, color = AppleOrange)
            }
        },
        confirmButton = { TextButton(onClick = { duration.toIntOrNull()?.let { onAdd(selectedType, it) } }) { Text("添加") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } }
    )
}
