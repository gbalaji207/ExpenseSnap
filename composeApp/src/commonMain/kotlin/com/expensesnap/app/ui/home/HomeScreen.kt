package com.expensesnap.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.expensesnap.app.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Receipt Scanner") }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Home Screen - Receipt List",
                fontSize = 18.sp
            )

            // TODO: Replace with actual receipt list
            // TODO: Implement receipt list with LazyColumn
            // TODO: Add pull-to-refresh functionality
            // TODO: Add empty state when no receipts

            // Test button to navigate to detail screen
            Button(
                onClick = {
                    navController.navigate(Screen.Detail("test-receipt-123"))
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("View Sample Receipt (Test)")
            }
        }
    }
}

