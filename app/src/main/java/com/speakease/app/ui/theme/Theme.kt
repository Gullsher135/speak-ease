package com.speakease.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF3949AB),
    onPrimary = Color.White,
    secondary = Color(0xFF00ACC1),
    tertiary = Color(0xFF8E24AA),
    background = Color(0xFFF4F7FF),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF141B2D)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8EA2FF),
    secondary = Color(0xFF50D4E7),
    tertiary = Color(0xFFDA9EFF),
    background = Color(0xFF0F1424),
    surface = Color(0xFF171F35),
    onSurface = Color(0xFFE1E8FF)
)

@Composable
fun SpeakEaseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
