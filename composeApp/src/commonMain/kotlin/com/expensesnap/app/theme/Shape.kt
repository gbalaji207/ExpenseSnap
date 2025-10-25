package com.expensesnap.app.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val ExpenseSnapShapes = Shapes(
    // Small components (chips, small buttons)
    extraSmall = RoundedCornerShape(4.dp),

    // Text fields, medium buttons
    small = RoundedCornerShape(8.dp),

    // Cards, large buttons
    medium = RoundedCornerShape(12.dp),

    // Dialogs, bottom sheets
    large = RoundedCornerShape(16.dp),

    // Full-screen modals
    extraLarge = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
)