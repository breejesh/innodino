package com.innodino.blocks.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DinoGreen,
    secondary = TechTeal,
    tertiary = SunYellow,
    background = SlateGray,
    surface = SlateGray,
    error = SoftCoral
)

private val LightColorScheme = lightColorScheme(
    primary = DinoGreen,
    onPrimary = PureWhite,
    primaryContainer = Color(0xFFD7F8E4),
    onPrimaryContainer = Color(0xFF002110),
    secondary = TechTeal,
    onSecondary = PureWhite,
    secondaryContainer = Color(0xFFB8E7FF),
    onSecondaryContainer = Color(0xFF001E2B),
    tertiary = SunYellow,
    onTertiary = SlateGray,
    tertiaryContainer = Color(0xFFFFECB3),
    onTertiaryContainer = Color(0xFF2E1B00),
    error = SoftCoral,
    errorContainer = Color(0xFFFFDAD6),
    onError = PureWhite,
    onErrorContainer = Color(0xFF410002),
    background = CloudWhite,
    onBackground = SlateGray,
    surface = PureWhite,
    onSurface = SlateGray,
    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = Color(0xFF666666),
    outline = Color(0xFFCCCCCC)
)

@Composable
fun InodinoBlocksTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = TechTeal.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
