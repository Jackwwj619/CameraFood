package com.caloriesnap.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AppleLightColorScheme = lightColorScheme(
    primary = AppleGreen,
    onPrimary = Color.White,
    primaryContainer = AppleGreen.copy(alpha = 0.15f),
    secondary = AppleBlue,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    error = AppleRed,
    outline = LightTextSecondary
)

private val AppleDarkColorScheme = darkColorScheme(
    primary = AppleGreen,
    onPrimary = Color.White,
    primaryContainer = AppleGreen.copy(alpha = 0.2f),
    secondary = AppleBlue,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    error = AppleRed,
    outline = DarkTextSecondary
)

@Composable
fun CalorieSnapTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) AppleDarkColorScheme else AppleLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
    MaterialTheme(colorScheme = colorScheme, content = content)
}
