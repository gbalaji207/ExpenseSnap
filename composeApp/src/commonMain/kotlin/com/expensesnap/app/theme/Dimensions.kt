package com.expensesnap.app.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Additional dimensions for specific UI components
 * Complements the Spacing object
 */
object Dimensions {
    // Button dimensions
    val buttonHeight: Dp = 48.dp
    val buttonMinWidth: Dp = 64.dp
    val buttonPaddingHorizontal: Dp = 16.dp

    // FAB dimensions
    val fabSize: Dp = 56.dp
    val fabIconSize: Dp = 24.dp

    // Card dimensions
    val cardCornerRadius: Dp = 12.dp
    val cardElevation: Dp = 2.dp
    val cardPadding: Dp = 16.dp

    // Text Field dimensions
    val textFieldHeight: Dp = 56.dp
    val textFieldCornerRadius: Dp = 4.dp
    val textFieldPadding: Dp = 16.dp

    // Receipt card dimensions
    val receiptCardHeight: Dp = 100.dp
    val receiptCardImageSize: Dp = 68.dp

    // Bottom sheet
    val bottomSheetPeekHeight: Dp = 80.dp
    val bottomSheetCornerRadius: Dp = 28.dp

    // Dialog dimensions
    val dialogMinWidth: Dp = 280.dp
    val dialogMaxWidth: Dp = 560.dp
    val dialogCornerRadius: Dp = 28.dp

    // App bar
    val topAppBarHeight: Dp = 64.dp
    val bottomNavBarHeight: Dp = 80.dp

    // Avatar/Profile
    val avatarSmall: Dp = 32.dp
    val avatarMedium: Dp = 40.dp
    val avatarLarge: Dp = 72.dp

    // Divider
    val dividerThickness: Dp = 1.dp

    // Border thickness
    val borderThin: Dp = 1.dp
    val borderMedium: Dp = 2.dp
}