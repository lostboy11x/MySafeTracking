package com.example.mysafetracking.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = OrangeDark,
    onPrimary = WhiteText,
    secondary = OrangeLight,
    background = OrangeBackground,
    surface = GraySurface,
    onBackground = BlackText,
    onSurface = BlackText
)

private val DarkColorScheme = darkColorScheme(
    primary = OrangeLight,
    onPrimary = BlackText,
    secondary = OrangeDark,
    background = BlackText,
    surface = GraySurface,
    onBackground = WhiteText,
    onSurface = WhiteText
)

@Composable
fun MySafeTrackingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}