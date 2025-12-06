package com.caloriesnap.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.caloriesnap.domain.model.MealType
import com.caloriesnap.domain.model.RecognizedFood
import com.caloriesnap.presentation.viewmodel.RecognitionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognitionScreen(navController: NavController, imageUri: String, mealTypeStr: String, viewModel: RecognitionViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedMeal by remember { mutableStateOf(MealType.valueOf(mealTypeStr)) }

    LaunchedEffect(Unit) { viewModel.setMealType(MealType.valueOf(mealTypeStr)) }
    LaunchedEffect(imageUri) { viewModel.recognize(imageUri) }
    LaunchedEffect(state.saved) { if (state.saved) navController.popBackStack("home", false) }

    Scaffold(topBar = {
        TopAppBar(title = { Text("识别结果") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "返回") }
        })
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MealType.entries.forEach { meal ->
                    FilterChip(
                        selected = selectedMeal == meal,
                        onClick = { selectedMeal = meal; viewModel.setMealType(meal) },
                        label = { Text(when (meal) { MealType.BREAKFAST -> "早餐"; MealType.LUNCH -> "午餐"; MealType.DINNER -> "晚餐"; MealType.SNACK -> "加餐" }) }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(8.dp))
                        Text("AI识别中...")
                    }
                }
                state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Error, "错误", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text(state.error ?: "未知错误", color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.recognize(imageUri) }) { Text("重试") }
                    }
                }
                else -> {
                    LazyColumn(Modifier.weight(1f)) {
                        itemsIndexed(state.foods) { index, food ->
                            FoodEditCard(food, onUpdate = { viewModel.updateFood(index, it) }, onDelete = { viewModel.removeFood(index) })
                        }
                        state.suggestion?.let { suggestion ->
                            item {
                                Spacer(Modifier.height(16.dp))
                                Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                                    Column(Modifier.padding(12.dp)) {
                                        Text("AI建议", style = MaterialTheme.typography.titleSmall)
                                        Text(suggestion, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                    if (state.foods.isNotEmpty()) {
                        Spacer(Modifier.height(16.dp))
                        val total = state.foods.sumOf { it.calories.toDouble() }.toFloat()
                        Text("总热量: ${total.toInt()} kcal", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.saveRecords() }, Modifier.fillMaxWidth()) { Text("保存记录") }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodEditCard(food: RecognizedFood, onUpdate: (RecognizedFood) -> Unit, onDelete: () -> Unit) {
    var weight by remember(food) { mutableStateOf(food.weight.toInt().toString()) }

    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(food.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "删除", tint = MaterialTheme.colorScheme.error) }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = weight,
                    onValueChange = { w ->
                        weight = w
                        w.toFloatOrNull()?.let { newWeight ->
                            val ratio = newWeight / food.weight
                            onUpdate(food.copy(weight = newWeight, calories = food.calories * ratio, protein = food.protein * ratio, carbs = food.carbs * ratio, fat = food.fat * ratio))
                        }
                    },
                    label = { Text("克数") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(100.dp),
                    singleLine = true
                )
                listOf(50, 100, 150, 200).forEach { g ->
                    FilterChip(selected = false, onClick = {
                        weight = g.toString()
                        val ratio = g.toFloat() / food.weight
                        onUpdate(food.copy(weight = g.toFloat(), calories = food.calories * ratio, protein = food.protein * ratio, carbs = food.carbs * ratio, fat = food.fat * ratio))
                    }, label = { Text("${g}g") })
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("${food.calories.toInt()} kcal")
                Text("蛋白${food.protein.toInt()}g 碳水${food.carbs.toInt()}g 脂肪${food.fat.toInt()}g", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
