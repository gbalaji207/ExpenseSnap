package com.expensesnap.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    // Primary colors
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = Blue500,
    onPrimaryContainer = White,

    // Secondary colors
    secondary = SecondaryLight,
    onSecondary = White,
    secondaryContainer = Green500,
    onSecondaryContainer = White,

    // Tertiary colors (using info/accent)
    tertiary = InfoColor,
    onTertiary = White,

    // Background colors
    background = BackgroundLight,
    onBackground = OnBackgroundLight,

    // Surface colors
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray600,

    // Error colors
    error = ErrorColor,
    onError = White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    // Outline colors
    outline = Gray200,
    outlineVariant = Gray300,

    // Inverse colors
    inverseSurface = Gray800,
    inverseOnSurface = White,
    inversePrimary = Blue500,

    // Container colors
    surfaceTint = PrimaryLight,
    scrim = Color.Black.copy(alpha = 0.32f)
)

private val DarkColorScheme = darkColorScheme(
    // Primary colors
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = Blue700,
    onPrimaryContainer = White,

    // Secondary colors
    secondary = SecondaryLight,
    onSecondary = Gray900,
    secondaryContainer = Green500,
    onSecondaryContainer = Gray900,

    // Tertiary colors
    tertiary = InfoColor,
    onTertiary = White,

    // Background colors
    background = BackgroundDark,
    onBackground = OnBackgroundDark,

    // Surface colors
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = Gray700,
    onSurfaceVariant = Gray400,

    // Error colors
    error = ErrorColor,
    onError = White,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    // Outline colors
    outline = Gray600,
    outlineVariant = Gray700,

    // Inverse colors
    inverseSurface = Gray50,
    inverseOnSurface = Gray900,
    inversePrimary = Blue600,

    // Container colors
    surfaceTint = PrimaryDark,
    scrim = Color.Black.copy(alpha = 0.32f)
)

@Composable
fun ExpenseSnapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ExpenseSnapTypography,
        shapes = ExpenseSnapShapes,
        content = content
    )
}

/**
 * Extended Material Theme colors for custom use cases
 */
object ExtendedColors {
    val success = SuccessColor
    val warning = WarningColor
    val info = InfoColor

    // Category-specific colors can be added here if needed
    // For MVP, categories use emojis instead of colors
}