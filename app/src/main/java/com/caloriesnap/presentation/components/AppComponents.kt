package com.caloriesnap.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.caloriesnap.presentation.theme.*

// iOS风格毛玻璃卡片
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    Box(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.12f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDark) listOf(
                        Color(0xFF2C2C2E).copy(alpha = 0.9f),
                        Color(0xFF1C1C1E).copy(alpha = 0.95f)
                    ) else listOf(
                        Color.White.copy(alpha = 0.95f),
                        Color(0xFFF8F8F8).copy(alpha = 0.9f)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

// 普通卡片
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

// 带动画的圆形进度条
@Composable
fun AnimatedCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1.5f),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progress"
    )
    CircularProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier.size(140.dp),
        strokeWidth = 12.dp,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        strokeCap = StrokeCap.Round,
        color = if (progress > 1f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    )
}

// 数字滚动动画
@Composable
fun AnimatedNumber(
    targetValue: Int,
    style: TextStyle = MaterialTheme.typography.headlineMedium
) {
    val animatedValue by animateIntAsState(
        targetValue = targetValue,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "number"
    )
    Text(text = "$animatedValue", style = style, fontWeight = FontWeight.Bold)
}
