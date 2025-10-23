package com.expensesnap.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.expensesnap.app.navigation.BottomNavBar
import com.expensesnap.app.navigation.NavGraph
import com.expensesnap.app.navigation.shouldShowBottomBar
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        Scaffold(
            bottomBar = {
                // Show bottom navigation only for main tab screens
                if (shouldShowBottomBar(currentDestination)) {
                    BottomNavBar(
                        navController = navController,
                        currentDestination = currentDestination
                    )
                }
            }
        ) { paddingValues ->
            // paddingValues are not used here because each screen has its own Scaffold
            // that handles its own padding. This outer Scaffold only manages the bottom bar.
            NavGraph(navController = navController)
        }
    }
}