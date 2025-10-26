package com.expensesnap.app.navigation

import androidx.navigation.NavDestination
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object Export : Screen()

    @Serializable
    data object Settings : Screen()

    @Serializable
    data object Scan : Screen()

    @Serializable
    data class Detail(val receiptId: String) : Screen()

    @Serializable
    data class Edit(val receiptId: String) : Screen()
}

// Helper function to check if route should show bottom navigation
fun shouldShowBottomBar(destination: NavDestination?): Boolean {
    return destination?.route in listOf(
        Screen.Home::class.qualifiedName,
        Screen.Export::class.qualifiedName,
        Screen.Settings::class.qualifiedName
    )
}

fun shouldShowFAB(destination: NavDestination?): Boolean {
    return destination?.route == Screen.Home::class.qualifiedName
}

