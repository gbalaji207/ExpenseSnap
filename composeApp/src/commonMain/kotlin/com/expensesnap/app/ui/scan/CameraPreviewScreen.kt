package com.expensesnap.app.ui.scan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashAuto
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kashif.cameraK.controller.CameraController
import com.kashif.cameraK.enums.CameraLens
import com.kashif.cameraK.enums.Directory
import com.kashif.cameraK.enums.FlashMode
import com.kashif.cameraK.enums.ImageFormat
import com.kashif.cameraK.result.ImageCaptureResult
import com.kashif.cameraK.ui.CameraPreview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Displays the live camera preview with controls for capturing photos.
 *
 * Features:
 * - Back camera preview
 * - Gallery access button
 * - Photo capture button
 * - Flash mode toggle (Auto/On/Off)
 * - Close button
 *
 * @param coroutineScope Coroutine scope for handling async camera operations
 * @param onPictureTaken Callback invoked with the captured image byte array
 * @param onClose Callback invoked when the user closes the camera
 */
@Composable
fun CameraPreviewScreen(
    coroutineScope: CoroutineScope,
    onPictureTaken: (ByteArray) -> Unit,
    onClose: () -> Unit
) {
    // Controller for managing camera operations (capture, flash, etc.)
    val cameraController = remember { mutableStateOf<CameraController?>(null) }
    // Current flash mode state (Auto, On, or Off)
    val flashMode = remember { mutableStateOf(FlashMode.AUTO) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Camera preview
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            cameraConfiguration = {
                setCameraLens(CameraLens.BACK)
                setFlashMode(FlashMode.AUTO)
                setImageFormat(ImageFormat.JPEG)
                setDirectory(Directory.PICTURES)
            },
            onCameraControllerReady = { cameraController.value = it },
        )

        // Close button overlay
        CloseButton(onClose)

        // Bottom buttons: Gallery, Capture, Flash
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .padding(bottom = 64.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gallery button (left)
            GalleryButton(onClick = {
                // TODO: Open gallery picker
            })

            // Capture button (center)
            CaptureButton(
                onClick = {
                    coroutineScope.launch {
                        // Capture image from camera
                        val result = cameraController.value?.takePicture()
                        when (result) {
                            is ImageCaptureResult.Success -> {
                                // Pass captured image bytes to parent
                                onPictureTaken(result.byteArray)
                            }

                            is ImageCaptureResult.Error -> {
                                // TODO: Handle capture error
                            }

                            null -> {
                                // Camera controller not ready
                            }
                        }
                    }
                }, modifier = Modifier.size(72.dp)
            )

            // Flash button (right)
            FlashButton(
                flashMode = flashMode.value,
            ) { newMode ->
                // Update flash mode in both state and camera controller
                flashMode.value = newMode
                cameraController.value?.setFlashMode(newMode)
            }
        }
    }
}

/**
 * Displays a circular close button positioned at the top-left corner.
 *
 * @param onClick Callback invoked when the close button is clicked
 */
@Composable
private fun BoxScope.CloseButton(onClick: () -> Unit) {
    CircularIconButton(
        onClick = onClick,
        modifier = Modifier.align(Alignment.TopStart).padding(top = 32.dp, start = 16.dp),
        icon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close camera",
            )
        },
    )
}

/**
 * Displays a circular button to toggle flash mode.
 *
 * Cycles through flash modes: OFF -> ON -> AUTO -> OFF
 *
 * @param modifier Modifier to apply to the button
 * @param flashMode Current flash mode
 * @param onFlashModeChange Callback invoked with the new flash mode when changed
 */
@Composable
private fun FlashButton(
    modifier: Modifier = Modifier,
    flashMode: FlashMode,
    onFlashModeChange: (FlashMode) -> Unit,
) {
    CircularIconButton(
        onClick = {
            // Cycle to next flash mode
            onFlashModeChange(flashMode.next())
        },
        modifier = modifier,
        icon = {
            Icon(
                imageVector = flashMode.imageVector(),
                contentDescription = "Flash",
                tint = Color.White,
            )
        },
    )
}

/**
 * Returns the appropriate icon for the current flash mode.
 *
 * @return ImageVector representing the flash mode icon
 */
private fun FlashMode.imageVector() = when (this) {
    FlashMode.ON -> Icons.Default.FlashOn
    FlashMode.OFF -> Icons.Default.FlashOff
    FlashMode.AUTO -> Icons.Default.FlashAuto
}

/**
 * Returns the next flash mode in the cycle: OFF -> ON -> AUTO -> OFF
 *
 * @return The next FlashMode in the sequence
 */
private fun FlashMode.next(): FlashMode = when (this) {
    FlashMode.OFF -> FlashMode.ON
    FlashMode.ON -> FlashMode.AUTO
    FlashMode.AUTO -> FlashMode.OFF
}

/**
 * Displays a circular button to access the device's photo gallery.
 *
 * @param onClick Callback invoked when the gallery button is clicked
 */
@Composable
private fun GalleryButton(onClick: () -> Unit) {
    CircularIconButton(
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Default.Photo,
                contentDescription = "Gallery",
                tint = Color.White,
            )
        },
    )
}

/**
 * A reusable circular icon button with customizable appearance.
 *
 * @param onClick Callback invoked when the button is clicked
 * @param icon Composable lambda to render the button's icon
 * @param modifier Modifier to apply to the button
 * @param backgroundColor Background color of the button
 * @param size Size of the button
 */
@Composable
private fun CircularIconButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White.copy(alpha = 0.3f),
    size: Dp = 44.dp
) {
    Surface(
        modifier = modifier.size(size), shape = CircleShape, color = backgroundColor
    ) {
        IconButton(
            onClick = onClick, modifier = Modifier.size(size)
        ) {
            icon()
        }
    }
}

/**
 * Displays a custom camera capture button with a circular design.
 *
 * The button consists of a white outline circle with a filled white circle inside,
 * mimicking the standard camera capture button design.
 *
 * @param onClick Callback invoked when the capture button is clicked
 * @param modifier Modifier to apply to the button
 * @param buttonSize Overall size of the clickable button area
 * @param iconSize Size of the visual icon (circle graphics)
 */
@Composable
private fun CaptureButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 72.dp,
    iconSize: Dp = 56.dp
) {
    Surface(
        modifier = modifier.size(buttonSize), shape = CircleShape, color = Color.Transparent
    ) {
        IconButton(
            onClick = onClick, modifier = Modifier.size(buttonSize)
        ) {
            // Custom camera capture button (filled circle with outline)
            Canvas(modifier = Modifier.size(iconSize)) {
                val canvasSize = size.minDimension
                val center = Offset(canvasSize / 2f, canvasSize / 2f)

                // Outer ring (outline)
                drawCircle(
                    color = Color.White,
                    radius = canvasSize / 2f,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )

                // Inner filled circle
                drawCircle(
                    color = Color.White, radius = canvasSize / 2.4f, center = center
                )
            }
        }
    }
}

