package com.example.mysafetracking.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/*
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
)*/

private val LightColorScheme = lightColorScheme(
    primary = TopGradientStart, // Utilitzem un dels colors de gradient
    onPrimary = Color.White, // Text blanc sobre el color primari
    secondary = ButtonGradientStart, // Utilitzem un altre color de gradient
    background = LightGray, // Utilitzem LightGray com a fons
    surface = Color.White, // Superfície blanca (o LightGray, si prefereixes)
    onBackground = DarkGrayText, // Text gris fosc sobre el fons
    onSurface = DarkGrayText, // Text gris fosc sobre la superfície
    // Pots afegir més colors si necessites utilitzar-los en altres parts del tema
)

// Colors per a DarkColorScheme (adaptats)
private val DarkColorScheme = darkColorScheme(
    primary = TopGradientEnd, // Utilitzem un altre color de gradient
    onPrimary = Color.Black, // Text negre sobre el color primari
    secondary = ButtonGradientEnd, // Utilitzem l'altre color de gradient
    background = Color.Black, // Fons negre
    surface = Color.DarkGray, // Superfície gris fosc
    onBackground = Color.White, // Text blanc sobre el fons
    onSurface = Color.White, // Text blanc sobre la superfície
    // Pots afegir més colors si necessites utilitzar-los en altres parts del tema
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