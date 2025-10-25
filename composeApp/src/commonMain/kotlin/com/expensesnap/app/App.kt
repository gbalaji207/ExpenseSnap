package com.expensesnap.app

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.expensesnap.app.navigation.BottomNavBar
import com.expensesnap.app.navigation.NavGraph
import com.expensesnap.app.navigation.shouldShowBottomBar
import com.expensesnap.app.navigation.shouldShowFAB
import com.expensesnap.app.theme.Dimensions
import com.expensesnap.app.theme.ExpenseSnapTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    ExpenseSnapTheme {
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
            },
            floatingActionButton = {
                // Show FAB only on Home screen
                if (shouldShowFAB(currentDestination)) {
                    FloatingActionButton(
                        onClick = {},
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Scan receipt",
                            modifier = Modifier.size(Dimensions.fabIconSize)
                        )
                    }
                }
            }
        ) { paddingValues ->
            // paddingValues are not used here because each screen has its own Scaffold
            // that handles its own padding. This outer Scaffold only manages the bottom bar.
            NavGraph(navController = navController)
        }
    }
}