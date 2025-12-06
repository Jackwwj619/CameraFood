package com.caloriesnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.caloriesnap.data.local.PreferencesManager
import com.caloriesnap.presentation.navigation.AppNavigation
import com.caloriesnap.presentation.theme.CalorieSnapTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by prefs.themeMode.collectAsState(initial = PreferencesManager.ThemeMode.SYSTEM)
            val darkTheme = when (themeMode) {
                PreferencesManager.ThemeMode.DARK -> true
                PreferencesManager.ThemeMode.LIGHT -> false
                PreferencesManager.ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            CalorieSnapTheme(darkTheme = darkTheme) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}
