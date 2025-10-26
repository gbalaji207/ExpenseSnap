package com.expensesnap.app.ui.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.icerock.moko.permissions.compose.BindEffect
import org.koin.compose.viewmodel.koinViewModel

/**
 * Main scan screen that handles the camera flow for capturing receipts.
 *
 * This screen manages multiple states including:
 * - Camera permission requests
 * - Live camera preview
 * - Captured image preview
 * - Image processing
 *
 * @param onClose Callback invoked when the user wants to close the scan screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(onReceiptSaved: (receiptId: String) -> Unit, onClose: () -> Unit) {
    val viewModel = koinViewModel<ScanViewModel>()
    val screenState by viewModel.scanScreenState.collectAsStateWithLifecycle()
    val showPermissionRationaleDialog by viewModel.showPermissionRationaleDialog.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    // Check camera permission when screen resumes
    LifecycleResumeEffect(Unit) {
        viewModel.checkCameraPermission()
        onPauseOrDispose { }
    }

    // Bind permission controller to lifecycle
    BindEffect(viewModel.permissionsController)

    // Handle different screen states
    when (screenState) {
        is ScanScreenState.Loading -> {
            // Show black screen mimicking camera preview while initializing
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {}
        }

        is ScanScreenState.CheckingPermission -> {
            // Display dialog requesting camera permission
            CameraPermissionDialog(
                onAllowClick = viewModel::requestCameraPermission,
                onDismiss = viewModel::cameraPermissionDialogDismissed,
            )
        }

        is ScanScreenState.AlternateUploadScreen -> {
            // Show alternative upload options when camera is not available
            AlternativeUploadScreen(
                onGalleryClick = {
                    // TODO: Open gallery picker
                }, onBackClick = onClose, onRetryCamera = viewModel::requestCameraPermission
            )
        }

        is ScanScreenState.CameraPreview -> {
            // Display live camera preview for capturing receipts
            CameraPreviewScreen(
                coroutineScope, onPictureTaken = { imageBytes ->
                    viewModel.onPhotoCaptured(imageBytes)
                }, onClose = onClose
            )
        }

        is ScanScreenState.CapturedImagePreview -> {
            // Show preview of captured image with options to use or retake
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
            // Display loading state while processing the captured image
            ProcessingScreen(viewModel.photoBytes!!)
        }

        is ScanScreenState.ImageProcessed -> {
            // Show success state after image processing completes
            SuccessScreen(
                onAnimationComplete = {
                    val receiptId = (screenState as ScanScreenState.ImageProcessed).receiptId
                    onReceiptSaved(receiptId)
                }
            )
        }
    }

    // Show settings dialog if user previously denied permission
    if (showPermissionRationaleDialog) {
        SettingsDialog(
            onOpenSettings = viewModel::openAppSettings,
            onDismiss = viewModel::dismissPermissionRationaleDialog
        )
    }
}