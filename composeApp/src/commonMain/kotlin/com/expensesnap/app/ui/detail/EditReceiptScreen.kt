package com.expensesnap.app.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditReceiptScreen(
    navController: NavController,
    receiptId: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Receipt") },
                navigationIcon = {
                    TextButton(onClick = { navController.navigateUp() }) {
                        Text("Cancel")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        // TODO: Save receipt changes
                        navController.navigateUp()
                    }) {
                        Text("Save")
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
                text = "Edit Receipt Screen",
                fontSize = 18.sp
            )

            Text(
                text = "Receipt ID: $receiptId",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            // TODO: Add editable merchant name field
            // TODO: Add date picker
            // TODO: Add amount input field
            // TODO: Add category dropdown
            // TODO: Add payment method selector
            // TODO: Add notes text field
            // TODO: Add ability to edit line items
            // TODO: Add image replacement option
            // TODO: Implement form validation
            // TODO: Save changes to database
        }
    }
}

