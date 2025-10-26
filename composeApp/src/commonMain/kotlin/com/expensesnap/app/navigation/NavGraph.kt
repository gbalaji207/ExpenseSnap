package com.expensesnap.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.expensesnap.app.ui.detail.EditReceiptScreen
import com.expensesnap.app.ui.detail.ReceiptDetailScreen
import com.expensesnap.app.ui.export.ExportScreen
import com.expensesnap.app.ui.home.HomeScreen
import com.expensesnap.app.ui.scan.ProcessingScreen
import com.expensesnap.app.ui.scan.ScanScreen
import com.expensesnap.app.ui.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = Screen.Home
    ) {
        // Main tab screens with bottom navigation
        composable<Screen.Home> {
            HomeScreen(navController = navController)
        }

        composable<Screen.Scan> {
            ScanScreen(onClose = { navController.navigateUp() })
        }

        composable<Screen.Export> {
            ExportScreen(navController = navController)
        }

        composable<Screen.Settings> {
            SettingsScreen(navController = navController)
        }

        // Secondary screens without bottom navigation
        composable<Screen.Detail> { backStackEntry ->
            val detail: Screen.Detail = backStackEntry.toRoute()
            ReceiptDetailScreen(
                navController = navController, receiptId = detail.receiptId
            )
        }

        composable<Screen.Edit> { backStackEntry ->
            val edit: Screen.Edit = backStackEntry.toRoute()
            EditReceiptScreen(
                navController = navController, receiptId = edit.receiptId
            )
        }

        composable<Screen.Processing> {
            ProcessingScreen(navController = navController)
        }
    }
}

