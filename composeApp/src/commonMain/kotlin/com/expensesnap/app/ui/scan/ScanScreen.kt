package com.expensesnap.app.ui.scan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kashif.cameraK.controller.CameraController
import com.kashif.cameraK.enums.CameraLens
import com.kashif.cameraK.enums.Directory
import com.kashif.cameraK.enums.FlashMode
import com.kashif.cameraK.enums.ImageFormat
import com.kashif.cameraK.result.ImageCaptureResult
import com.kashif.cameraK.ui.CameraPreview
import dev.icerock.moko.permissions.compose.BindEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(onClose: () -> Unit) {
    val viewModel = koinViewModel<ScanViewModel>()
    val screenState by viewModel.scanScreenState.collectAsStateWithLifecycle()
    val showPermissionRationaleDialog by viewModel.showPermissionRationaleDialog.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    LifecycleResumeEffect(Unit) {
        viewModel.checkCameraPermission()
        onPauseOrDispose { }
    }

    BindEffect(viewModel.permissionsController)

    when (screenState) {
        is ScanScreenState.Loading -> {
            // show black screen mimicking camera preview
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {}
        }

        is ScanScreenState.CheckingPermission -> {
            CameraPermissionDialog(
                onAllowClick = viewModel::requestCameraPermission,
                onDismiss = viewModel::cameraPermissionDialogDismissed,
            )
        }

        is ScanScreenState.AlternateUploadScreen -> {
            AlternativeUploadScreen(
                onGalleryClick = {
                    // TODO: Open gallery picker
                }, onBackClick = onClose, onRetryCamera = viewModel::requestCameraPermission
            )
        }

        is ScanScreenState.CameraPreview -> {
            CameraPreview(
                coroutineScope, onPictureTaken = { imageBytes ->
                    viewModel.onPhotoCaptured(imageBytes)
                }, onClose = onClose
            )
        }

        is ScanScreenState.CapturedImagePreview -> {
            val bytes = viewModel.photoBytes
            if (bytes != null) {
                CapturedImagePreview(
                    bytes = bytes,
                    onUsePhoto = viewModel::processCapturedPhoto,
                    onRetake = viewModel::retakePhoto
                )
            } else {
                onClose()
            }
        }

        is ScanScreenState.ProcessingImagePreview -> {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text("Processing photo...", style = MaterialTheme.typography.bodyLarge)
            }
        }

        is ScanScreenState.ImageProcessed -> {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text("Processed Successfully", style = MaterialTheme.typography.bodyLarge)
            }
        }

        ScanScreenState.Close -> {
            onClose()
        }
    }

    if (showPermissionRationaleDialog) {
        SettingsDialog(
            onOpenSettings = viewModel::openAppSettings,
            onDismiss = viewModel::dismissPermissionRationaleDialog
        )
    }
}

@Composable
private fun CameraPreview(
    coroutineScope: CoroutineScope, onPictureTaken: (ByteArray) -> Unit, onClose: () -> Unit
) {
    val cameraController = remember { mutableStateOf<CameraController?>(null) }
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
                        val result = cameraController.value?.takePicture()
                        when (result) {
                            is ImageCaptureResult.Success -> {
                                onPictureTaken(result.byteArray)
                            }

                            is ImageCaptureResult.Error -> {
                            }

                            null -> {
                            }
                        }
                    }
                }, modifier = Modifier.size(72.dp)
            )

            // Flash button (right)
            FlashButton(
                flashMode = flashMode.value,
            ) { newMode ->
                flashMode.value = newMode
                cameraController.value?.setFlashMode(newMode)
            }
        }
    }
}

@Composable
fun CapturedImagePreview(bytes: ByteArray, onUsePhoto: () -> Unit, onRetake: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        val imageBitmap = remember(bytes) { bytes.decodeToImageBitmap() }

        Image(
            bitmap = imageBitmap,
            contentDescription = "Captured image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

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

@Composable
private fun FlashButton(
    modifier: Modifier = Modifier,
    flashMode: FlashMode,
    onFlashModeChange: (FlashMode) -> Unit,
) {
    CircularIconButton(
        onClick = {
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

fun FlashMode.imageVector() = when (this) {
    FlashMode.ON -> Icons.Default.FlashOn
    FlashMode.OFF -> Icons.Default.FlashOff
    FlashMode.AUTO -> Icons.Default.FlashAuto
}

fun FlashMode.next(): FlashMode = when (this) {
    FlashMode.OFF -> FlashMode.ON
    FlashMode.ON -> FlashMode.AUTO
    FlashMode.AUTO -> FlashMode.OFF
}

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

@Composable
private fun CaptureButton(
    onClick: () -> Unit, modifier: Modifier = Modifier, buttonSize: Dp = 72.dp, iconSize: Dp = 56.dp
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
