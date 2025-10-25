package com.expensesnap.app.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing system based on 8dp base unit
 * Following Material Design spacing guidelines
 */
object Spacing {
    // Base unit: 8dp
    private val baseUnit = 8.dp

    // Common spacing values
    val extraSmall: Dp = 4.dp      // Extra Small (0.5x base)
    val small: Dp = 8.dp           // Small (1x base)
    val medium: Dp = 16.dp         // Medium (2x base) - DEFAULT
    val large: Dp = 24.dp          // Large (3x base)
    val extraLarge: Dp = 32.dp     // Extra Large (4x base)
    val xxLarge: Dp = 48.dp        // XX Large (6x base) - Section breaks

    // Component-specific spacing
    val screenPadding: Dp = 16.dp
    val cardPadding: Dp = 16.dp
    val cardMargin: Dp = 8.dp
    val sectionSpacing: Dp = 24.dp
    val formFieldSpacing: Dp = 16.dp

    // Icon sizes
    val iconSmall: Dp = 16.dp      // Inline with text
    val iconMedium: Dp = 24.dp     // Navigation, actions
    val iconLarge: Dp = 32.dp      // Large icons
    val iconExtraLarge: Dp = 48.dp // Welcome screens
    val iconXXLarge: Dp = 64.dp    // Empty states

    // Touch targets
    val minTouchTarget: Dp = 48.dp // Minimum button/input height

    // Elevation (shadows)
    val elevationNone: Dp = 0.dp
    val elevationLevel1: Dp = 2.dp  // Cards, buttons
    val elevationLevel2: Dp = 4.dp  // FAB, raised buttons
    val elevationLevel3: Dp = 8.dp  // App bar, dialogs
    val elevationLevel4: Dp = 16.dp // Modals
}