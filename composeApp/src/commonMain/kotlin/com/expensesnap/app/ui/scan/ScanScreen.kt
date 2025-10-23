package com.expensesnap.app.ui.scan

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
fun ScanScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Receipt") }
            )
        }
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
                text = "Scan Screen - Camera/Upload",
                fontSize = 18.sp
            )

            // TODO: Implement camera preview
            // TODO: Add capture button
            // TODO: Add gallery picker button
            // TODO: Add image preview after capture
            // TODO: Implement camera permissions

            // Test button to navigate to processing screen
            Button(
                onClick = {
                    navController.navigate(Screen.Processing)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Start Processing (Test)")
            }
        }
    }
}

