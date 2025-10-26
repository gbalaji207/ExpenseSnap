package com.expensesnap.app.ui.scan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

/**
 * Displays a preview of the captured image with options to use or retake the photo.
 *
 * @param bytes The captured image as a byte array
 * @param onUsePhoto Callback invoked when the user confirms using the photo
 * @param onRetake Callback invoked when the user wants to retake the photo
 */
@Composable
fun CapturedImagePreview(bytes: ByteArray, onUsePhoto: () -> Unit, onRetake: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Decode byte array to ImageBitmap for display
        val imageBitmap = remember(bytes) { bytes.decodeToImageBitmap() }

        // Display captured image
        Image(
            bitmap = imageBitmap,
            contentDescription = "Captured image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Action buttons at bottom
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .padding(bottom = 48.dp, start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextButton(onClick = onRetake) {
                Text("Retake", color = Color.White)
            }

            TextButton(onClick = onUsePhoto) {
                Text("Use Photo", color = Color.White)
            }
        }
    }
}

