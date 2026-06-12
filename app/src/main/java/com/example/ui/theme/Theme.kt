package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = AgrobankLightGreen,
    secondary = AgrobankGreen,
    tertiary = AgrobankAccentGold,
    background = AgrobankTextPrimary,
    surface = AgrobankSecondary,
    onPrimary = AgrobankDarkGreen,
    onSecondary = AgrobankSurface,
    onBackground = AgrobankBackground,
    onSurface = AgrobankBackground,
    outline = AgrobankAccentSilver
  )

private val LightColorScheme =
  lightColorScheme(
    primary = AgrobankGreen,
    secondary = AgrobankDarkGreen,
    tertiary = AgrobankAccentGold,
    background = AgrobankBackground,
    surface = AgrobankSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = AgrobankTextPrimary,
    onSurface = AgrobankTextPrimary,
    outline = AgrobankAccentSilver,
    surfaceVariant = AgrobankLightGreen
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
