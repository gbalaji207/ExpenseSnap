package com.expensesnap.app.ui.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.expensesnap.app.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun ProcessingScreen(navController: NavController) {
    // Full screen without top bar
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp)
        )

        Text(
            text = "Processing Screen - AI Extraction",
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Analyzing receipt...",
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        // TODO: Implement AI extraction logic
        // TODO: Show progress indicators
        // TODO: Handle extraction errors
        // TODO: Navigate to detail screen after successful extraction

        // Simulate processing and navigate back (for testing)
        LaunchedEffect(Unit) {
            delay(3000) // Simulate 3 seconds of processing
            navController.navigate(Screen.Detail(receiptId = "new-receipt-456")) {
                popUpTo<Screen.Scan>()
            }
        }
    }
}

