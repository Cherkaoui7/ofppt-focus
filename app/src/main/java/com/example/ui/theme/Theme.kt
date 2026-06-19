package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = TechTeal, // `#0061A4` (or light blue equivalent)
    secondary = BrandBlueLight, // `#D1E4FF`
    tertiary = CyberPurple, // `#00A3BF`
    background = CyberDarkBg,
    surface = CyberDarkCard,
    surfaceVariant = CyberDarkBorder,
    onPrimary = Color.White,
    onSecondary = DeepNavyText,
    onBackground = Color(0xFFFDFBFF),
    onSurface = Color(0xFFF0F4F8),
    outline = CyberDarkBorder
  )

private val LightColorScheme =
  lightColorScheme(
    primary = TechTeal, // `#0061A4`
    secondary = BrandBlueLight, // `#D1E4FF`
    tertiary = CyberPurple, // `#00A3BF`
    background = CyberLightBg, // `#FDFBFF`
    surface = CyberLightCard, // `#FFFFFF`
    surfaceVariant = CyberLightBorder, // `#DDE3EA`
    onPrimary = Color.White,
    onSecondary = DeepNavyText,
    onBackground = DeepNavyText,
    onSurface = DeepNavyText,
    outline = CyberLightBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disable dynamic color to enforce beautiful cyber-slate brand consistency
  dynamicColor: Boolean = false,
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
