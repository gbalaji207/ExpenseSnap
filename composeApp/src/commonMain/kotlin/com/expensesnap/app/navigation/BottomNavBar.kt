package com.expensesnap.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, Icons.Default.Home, "Home"),
    BottomNavItem(Screen.Scan, Icons.Default.CameraAlt, "Scan"),
    BottomNavItem(Screen.Export, Icons.Default.FileDownload, "Export"),
    BottomNavItem(Screen.Settings, Icons.Default.Settings, "Settings")
)

@Composable
fun BottomNavBar(
    navController: NavController,
    currentDestination: NavDestination?
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = when (currentDestination?.route) {
                Screen.Home::class.qualifiedName -> item.screen is Screen.Home
                Screen.Scan::class.qualifiedName -> item.screen is Screen.Scan
                Screen.Export::class.qualifiedName -> item.screen is Screen.Export
                Screen.Settings::class.qualifiedName -> item.screen is Screen.Settings
                else -> false
            }

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.screen) {
                            // Pop up to the start destination to avoid building up a large stack
                            popUpTo(Screen.Home) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when navigating back to a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

