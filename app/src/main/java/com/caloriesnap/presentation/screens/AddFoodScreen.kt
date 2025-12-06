package com.caloriesnap.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.caloriesnap.data.local.DefaultFoods
import com.caloriesnap.domain.model.Food
import com.caloriesnap.domain.model.MealType
import com.caloriesnap.presentation.navigation.Screen
import com.caloriesnap.presentation.viewmodel.AddFoodViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(navController: NavController, mealTypeStr: String, viewModel: AddFoodViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val mealType = MealType.valueOf(mealTypeStr)
    var selectedFood by remember { mutableStateOf<Food?>(null) }
    var weight by remember { mutableStateOf("100") }
    var showCustomDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("主食") }
    val categories = remember { DefaultFoods.categories.keys.toList() }

    LaunchedEffect(state.saved) { if (state.saved) { viewModel.resetSaved(); navController.popBackStack() } }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("添加食物") },
            navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "返回") } },
            actions = {
                IconButton(onClick = { navController.navigate(Screen.Camera.createRoute(mealTypeStr)) }) { Icon(Icons.Default.CameraAlt, "拍照") }
                IconButton(onClick = { showCustomDialog = true }) { Icon(Icons.Default.Add, "自定义") }
            }
        )
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.search(it) },
                label = { Text("搜索食物") },
                leadingIcon = { Icon(Icons.Default.Search, "搜索") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            if (state.searchQuery.isEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        FilterChip(selected = selectedCategory == cat, onClick = { selectedCategory = cat }, label = { Text(cat) })
                    }
                }
                Spacer(Modifier.height(8.dp))
                LazyColumn(Modifier.weight(1f)) {
                    val foods = DefaultFoods.categories[selectedCategory] ?: emptyList()
                    items(foods) { entity ->
                        val food = Food(entity.id, entity.name, entity.calories, entity.protein, entity.carbs, entity.fat)
                        FoodListItem(food, state.favorites.any { it.name == food.name }, { selectedFood = food }) { viewModel.toggleFavorite(food) }
                    }
                }
            } else {
                LazyColumn(Modifier.weight(1f)) {
                    items(state.searchResults) { food -> FoodListItem(food, state.favorites.any { it.id == food.id }, { selectedFood = food }) { viewModel.toggleFavorite(food) } }
                }
            }
        }
    }

    selectedFood?.let { food ->
        AlertDialog(
            onDismissRequest = { selectedFood = null },
            title = { Text(food.name) },
            text = {
                Column {
                    Text("每100g: ${food.calories.toInt()}kcal | 蛋白${food.protein.toInt()}g 碳水${food.carbs.toInt()}g 脂肪${food.fat.toInt()}g")
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("克数") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(50, 100, 150, 200).forEach { g ->
                            FilterChip(selected = weight == g.toString(), onClick = { weight = g.toString() }, label = { Text("${g}g") })
                        }
                    }
                    weight.toFloatOrNull()?.let { w ->
                        val cal = food.calories * w / 100
                        Spacer(Modifier.height(8.dp))
                        Text("热量: ${cal.toInt()} kcal", style = MaterialTheme.typography.titleMedium)
                    }
                }
            },
            confirmButton = { TextButton(onClick = { weight.toFloatOrNull()?.let { viewModel.addFood(food, it, mealType) }; selectedFood = null }) { Text("添加") } },
            dismissButton = { TextButton(onClick = { selectedFood = null }) { Text("取消") } }
        )
    }

    if (showCustomDialog) {
        CustomFoodDialog(onDismiss = { showCustomDialog = false }) { name, cal, pro, carb, fat, w ->
            viewModel.addCustomFood(name, cal, pro, carb, fat, w, mealType)
            showCustomDialog = false
        }
    }
}

@Composable
fun FoodListItem(food: Food, isFavorite: Boolean, onClick: () -> Unit, onFavorite: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(vertical = 2.dp).clickable(onClick = onClick)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(food.name, style = MaterialTheme.typography.titleMedium)
                Text("${food.calories.toInt()}kcal/100g", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onFavorite) {
                Icon(if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "收藏",
                    tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@Composable
fun CustomFoodDialog(onDismiss: () -> Unit, onConfirm: (String, Float, Float, Float, Float, Float) -> Unit) {
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("0") }
    var carbs by remember { mutableStateOf("0") }
    var fat by remember { mutableStateOf("0") }
    var weight by remember { mutableStateOf("100") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("自定义食物") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("食物名称") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = calories, onValueChange = { calories = it }, label = { Text("热量(每100g)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = protein, onValueChange = { protein = it }, label = { Text("蛋白质") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = carbs, onValueChange = { carbs = it }, label = { Text("碳水") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = fat, onValueChange = { fat = it }, label = { Text("脂肪") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.weight(1f))
                }
                OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("本次克数") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = { TextButton(onClick = { if (name.isNotBlank() && calories.toFloatOrNull() != null) onConfirm(name, calories.toFloat(), protein.toFloatOrNull() ?: 0f, carbs.toFloatOrNull() ?: 0f, fat.toFloatOrNull() ?: 0f, weight.toFloatOrNull() ?: 100f) }) { Text("添加") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } }
    )
}
