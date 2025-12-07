package com.caloriesnap.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.caloriesnap.domain.model.*
import com.caloriesnap.presentation.components.GlassCard
import com.caloriesnap.presentation.navigation.Screen
import com.caloriesnap.presentation.theme.*
import com.caloriesnap.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showWeightDialog by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }
    var profile by remember(state.profile) { mutableStateOf(state.profile) }

    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(ApplePink.copy(alpha = 0.1f), AppleIndigo.copy(alpha = 0.08f), MaterialTheme.colorScheme.background)
            )
        )
    ) {
        Scaffold(containerColor = Color.Transparent) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("我的", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) { Icon(Icons.Default.Settings, "设置", tint = MaterialTheme.colorScheme.outline) }
                }
                Spacer(Modifier.height(16.dp))
                GlassCard(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("身体数据", style = MaterialTheme.typography.titleMedium)
                        IconButton(onClick = { editMode = !editMode }) { Icon(if (editMode) Icons.Default.Check else Icons.Default.Edit, "编辑") }
                    }
                    Spacer(Modifier.height(12.dp))
                    if (editMode) {
                        ProfileEditForm(profile) { profile = it }
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.updateProfile(profile); editMode = false }, Modifier.fillMaxWidth()) { Text("保存") }
                    } else {
                        ProfileInfo(state.profile)
                    }
                }
                Spacer(Modifier.height(16.dp))

                GlassCard(Modifier.fillMaxWidth()) {
                    Text("能量计算", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    InfoRow("基础代谢(BMR)", "${state.bmr.toInt()} kcal")
                    InfoRow("每日消耗(TDEE)", "${state.tdee.toInt()} kcal")
                    InfoRow("目标热量", "${state.targetCalories.toInt()} kcal")
                }
                Spacer(Modifier.height(16.dp))

                GlassCard(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("体重记录", style = MaterialTheme.typography.titleMedium)
                        IconButton(onClick = { showWeightDialog = true }) { Icon(Icons.Default.Add, "添加") }
                    }
                    if (state.weightRecords.isEmpty()) {
                        Text("暂无记录", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                    } else {
                        if (state.weightRecords.size >= 2) {
                            Spacer(Modifier.height(8.dp))
                            WeightTrendChart(state.weightRecords.takeLast(14))
                            Spacer(Modifier.height(8.dp))
                        }
                        state.weightRecords.take(5).forEach { record ->
                            InfoRow(record.date, "${record.weight} kg")
                        }
                    }
                }
            }
        }
    }

    if (showWeightDialog) {
        var weight by remember { mutableStateOf(state.profile.weight.toString()) }
        AlertDialog(
            onDismissRequest = { showWeightDialog = false },
            title = { Text("记录体重") },
            text = { OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("体重(kg)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true) },
            confirmButton = { TextButton(onClick = { weight.toFloatOrNull()?.let { viewModel.addWeightRecord(it) }; showWeightDialog = false }) { Text("保存") } },
            dismissButton = { TextButton(onClick = { showWeightDialog = false }) { Text("取消") } }
        )
    }
}

@Composable
fun ProfileInfo(profile: UserProfile) {
    InfoRow("性别", if (profile.gender == Gender.MALE) "男" else "女")
    InfoRow("年龄", "${profile.age} 岁")
    InfoRow("身高", "${profile.height.toInt()} cm")
    InfoRow("体重", "${profile.weight} kg")
    InfoRow("活动水平", when (profile.activityLevel) { ActivityLevel.SEDENTARY -> "久坐"; ActivityLevel.LIGHT -> "轻度活动"; ActivityLevel.MODERATE -> "中度活动"; ActivityLevel.HEAVY -> "重度活动"; ActivityLevel.ATHLETE -> "专业运动员" })
    InfoRow("目标", when (profile.goal) { Goal.LOSE -> "减脂"; Goal.MAINTAIN -> "维持"; Goal.GAIN -> "增肌" })
    InfoRow("热量调整", "${profile.calorieAdjustment} kcal")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditForm(profile: UserProfile, onUpdate: (UserProfile) -> Unit) {
    var age by remember { mutableStateOf(profile.age.toString()) }
    var height by remember { mutableStateOf(profile.height.toInt().toString()) }
    var weight by remember { mutableStateOf(profile.weight.toString()) }
    var calorieAdj by remember { mutableStateOf(profile.calorieAdjustment.toString()) }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(selected = profile.gender == Gender.MALE, onClick = { onUpdate(profile.copy(gender = Gender.MALE)) }, label = { Text("男") })
        FilterChip(selected = profile.gender == Gender.FEMALE, onClick = { onUpdate(profile.copy(gender = Gender.FEMALE)) }, label = { Text("女") })
    }
    Spacer(Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = age, onValueChange = { age = it; it.toIntOrNull()?.let { v -> onUpdate(profile.copy(age = v)) } }, label = { Text("年龄") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f))
        OutlinedTextField(value = height, onValueChange = { height = it; it.toFloatOrNull()?.let { v -> onUpdate(profile.copy(height = v)) } }, label = { Text("身高cm") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f))
        OutlinedTextField(value = weight, onValueChange = { weight = it; it.toFloatOrNull()?.let { v -> onUpdate(profile.copy(weight = v)) } }, label = { Text("体重kg") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true, modifier = Modifier.weight(1f))
    }
    Spacer(Modifier.height(8.dp))
    Text("活动水平", style = MaterialTheme.typography.bodySmall)
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        listOf(ActivityLevel.SEDENTARY to "久坐", ActivityLevel.LIGHT to "轻度", ActivityLevel.MODERATE to "中度", ActivityLevel.HEAVY to "重度", ActivityLevel.ATHLETE to "运动员").forEach { (level, name) ->
            FilterChip(selected = profile.activityLevel == level, onClick = { onUpdate(profile.copy(activityLevel = level)) }, label = { Text(name, style = MaterialTheme.typography.labelSmall) })
        }
    }
    Spacer(Modifier.height(8.dp))
    Text("目标", style = MaterialTheme.typography.bodySmall)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(Goal.LOSE to "减脂", Goal.MAINTAIN to "维持", Goal.GAIN to "增肌").forEach { (goal, name) ->
            FilterChip(selected = profile.goal == goal, onClick = { onUpdate(profile.copy(goal = goal)) }, label = { Text(name) })
        }
    }
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(value = calorieAdj, onValueChange = { calorieAdj = it; it.toIntOrNull()?.let { v -> onUpdate(profile.copy(calorieAdjustment = v)) } }, label = { Text("热量缺口/盈余(kcal)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.fillMaxWidth())
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.outline)
        Text(value)
    }
}

@Composable
fun WeightTrendChart(records: List<WeightRecord>) {
    if (records.size < 2) return
    val minW = records.minOf { it.weight } - 1
    val maxW = records.maxOf { it.weight } + 1
    val primaryColor = MaterialTheme.colorScheme.primary

    androidx.compose.foundation.Canvas(Modifier.fillMaxWidth().height(100.dp)) {
        val stepX = size.width / (records.size - 1)
        val points = records.mapIndexed { i, r ->
            androidx.compose.ui.geometry.Offset(i * stepX, size.height - ((r.weight - minW) / (maxW - minW)) * size.height)
        }
        for (i in 0 until points.size - 1) {
            drawLine(primaryColor, points[i], points[i + 1], strokeWidth = 3f)
        }
        points.forEach { drawCircle(primaryColor, 6f, it) }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("${minW.toInt()}kg", style = MaterialTheme.typography.labelSmall)
        Text("${maxW.toInt()}kg", style = MaterialTheme.typography.labelSmall)
    }
}
