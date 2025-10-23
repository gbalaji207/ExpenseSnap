package com.expensesnap.app.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun ReceiptDetailScreen(
    navController: NavController,
    receiptId: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Receipt Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Edit(receiptId = receiptId))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                    IconButton(onClick = { /* TODO: Show more menu */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More"
                        )
                    }
                }
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
                text = "Receipt Detail Screen",
                fontSize = 18.sp
            )

            Text(
                text = "Receipt ID: $receiptId",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            // TODO: Display receipt image
            // TODO: Show merchant name
            // TODO: Show date and time
            // TODO: Show total amount
            // TODO: Show category
            // TODO: Show payment method
            // TODO: Show notes
            // TODO: Show line items
            // TODO: Add delete functionality in more menu

            // Test button to navigate to edit screen
            Button(
                onClick = {
                    navController.navigate(Screen.Edit(receiptId = receiptId))
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Edit Receipt (Test)")
            }
        }
    }
}

