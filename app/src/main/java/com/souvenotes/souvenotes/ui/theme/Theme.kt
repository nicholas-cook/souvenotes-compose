package com.souvenotes.souvenotes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.souvenotes.souvenotes.repository.prefs.AppThemePref

private val DarkColorPalette = darkColors(
    primary = SouvenotesYellow,
    secondary = SouvenotesYellow
)

private val LightColorPalette = lightColors(
    primary = SouvenotesBrown,
    primaryVariant = SouvenotesBrown,
    secondary = SouvenotesBrown,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun SouvenotesTheme(appThemePref: AppThemePref, content: @Composable () -> Unit) {
    val colors = when (appThemePref) {
        AppThemePref.Light -> LightColorPalette
        AppThemePref.Dark -> DarkColorPalette
        AppThemePref.System -> if (isSystemInDarkTheme()) {
            DarkColorPalette
        } else {
            LightColorPalette
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}