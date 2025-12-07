package com.caloriesnap.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.caloriesnap.presentation.screens.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Camera : Screen("camera/{mealType}") {
        fun createRoute(mealType: String) = "camera/$mealType"
    }
    object Recognition : Screen("recognition/{imageUri}/{mealType}") {
        fun createRoute(uri: String, mealType: String) = "recognition/$uri/$mealType"
    }
    object AddFood : Screen("add_food/{mealType}") {
        fun createRoute(mealType: String) = "add_food/$mealType"
    }
    object History : Screen("history")
    object Statistics : Screen("statistics")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Exercise : Screen("exercise")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val bottomNavItems = listOf(
        Triple(Screen.Home.route, "首页", Icons.Default.Home),
        Triple(Screen.History.route, "记录", Icons.Default.DateRange),
        Triple(Screen.Statistics.route, "统计", Icons.Default.BarChart),
        Triple(Screen.Profile.route, "我的", Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavItems.map { it.first }) {
                NavigationBar {
                    bottomNavItems.forEach { (route, label, icon) ->
                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = { if (currentRoute != route) navController.navigate(route) { popUpTo(Screen.Home.route) } },
                            icon = { Icon(icon, label) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding),
            enterTransition = { fadeIn(tween(300)) + slideInHorizontally(initialOffsetX = { 30 }, animationSpec = tween(300)) },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(300)) },
            popExitTransition = { fadeOut(tween(200)) + slideOutHorizontally(targetOffsetX = { 30 }, animationSpec = tween(300)) }
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Camera.route, listOf(navArgument("mealType") { type = NavType.StringType })) {
                CameraScreen(navController, it.arguments?.getString("mealType") ?: "BREAKFAST")
            }
            composable(Screen.Recognition.route, listOf(navArgument("imageUri") { type = NavType.StringType }, navArgument("mealType") { type = NavType.StringType })) {
                RecognitionScreen(navController, it.arguments?.getString("imageUri") ?: "", it.arguments?.getString("mealType") ?: "BREAKFAST")
            }
            composable(Screen.AddFood.route, listOf(navArgument("mealType") { type = NavType.StringType })) {
                AddFoodScreen(navController, it.arguments?.getString("mealType") ?: "BREAKFAST")
            }
            composable(Screen.History.route) { HistoryScreen(navController) }
            composable(Screen.Statistics.route) { StatisticsScreen() }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
            composable(Screen.Exercise.route) { ExerciseScreen(navController) }
        }
    }
}
