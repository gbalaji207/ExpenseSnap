package com.expensesnap.app.ui.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensesnap.app.data.repository.AppPreferencesRepository
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.camera.CAMERA
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the scan screen that manages camera permissions, photo capture, and processing flow.
 *
 * This ViewModel coordinates the entire scanning workflow including:
 * - Checking and requesting camera permissions
 * - Managing different screen states (loading, camera, preview, processing)
 * - Handling captured photo data
 * - Processing images
 *
 * @param permissionsController Controller for managing runtime permissions
 * @param appPreferencesRepository Repository for storing app preferences like permission request history
 */
class ScanViewModel(
    internal val permissionsController: PermissionsController,
    private val appPreferencesRepository: AppPreferencesRepository
) : ViewModel() {

    // Private mutable state for the current screen state
    private val _scanScreenState = MutableStateFlow<ScanScreenState>(ScanScreenState.Loading)

    /**
     * Current state of the scan screen. Observed by the UI to determine what to display.
     */
    val scanScreenState: StateFlow<ScanScreenState> = _scanScreenState.asStateFlow()

    // Private mutable state for showing the permission rationale dialog
    private val _showPermissionRationaleDialog = MutableStateFlow(false)

    /**
     * Whether to show the settings dialog when permission is permanently denied.
     */
    val showPermissionRationaleDialog: StateFlow<Boolean> =
        _showPermissionRationaleDialog.asStateFlow()

    /**
     * Stores the captured photo as a byte array until it's processed or discarded.
     */
    var photoBytes: ByteArray? = null

    /**
     * Checks if camera permission is granted and updates the screen state accordingly.
     *
     * Flow:
     * - If permission is granted → Show camera preview
     * - If permission was previously requested and denied → Show alternative upload options
     * - If permission was never requested → Show permission request dialog
     */
    fun checkCameraPermission() {
        viewModelScope.launch {
            if (permissionsController.isPermissionGranted(Permission.CAMERA)) {
                // Permission already granted, go directly to camera
                _scanScreenState.update { ScanScreenState.CameraPreview }
            } else {
                // Check if we've asked for permission before
                val permissionAlreadyRequested =
                    appPreferencesRepository.cameraPermissionRequested.firstOrNull()
                if (permissionAlreadyRequested == true) {
                    // User previously denied, show alternative options
                    _scanScreenState.update { ScanScreenState.AlternateUploadScreen }
                } else {
                    // First time, show permission request dialog
                    _scanScreenState.update { ScanScreenState.CheckingPermission }
                }
            }
        }
    }

    /**
     * Requests camera permission from the user and handles the result.
     *
     * This function:
     * - Marks that permission has been requested (to avoid repeated prompts)
     * - Attempts to get permission from the user
     * - Handles different denial scenarios (temporary vs permanent)
     * - Updates screen state based on the result
     *
     * @throws DeniedAlwaysException When user permanently denies permission (shows settings dialog)
     * @throws DeniedException When user temporarily denies permission (shows alternative upload)
     */
    fun requestCameraPermission() {
        viewModelScope.launch {
            // Mark that we've requested permission to avoid re-showing dialog on app restart
            appPreferencesRepository.setCameraPermissionRequested(true)
            try {
                // Request permission from the user
                permissionsController.providePermission(Permission.CAMERA)
            } catch (_: DeniedAlwaysException) {
                // User selected "Don't ask again" - show dialog to open settings
                _showPermissionRationaleDialog.update { true }
            } catch (_: DeniedException) {
                // User denied permission this time - show alternative upload options
                _scanScreenState.update { ScanScreenState.AlternateUploadScreen }
            } finally {
                // If permission was granted, proceed to camera
                if (permissionsController.isPermissionGranted(Permission.CAMERA)) {
                    _scanScreenState.update { ScanScreenState.CameraPreview }
                }
            }
        }
    }

    /**
     * Called when the user dismisses the camera permission dialog without granting permission.
     * Transitions to the alternative upload screen where users can select from gallery.
     */
    fun cameraPermissionDialogDismissed() {
        _scanScreenState.update { ScanScreenState.AlternateUploadScreen }
    }

    /**
     * Dismisses the permission rationale dialog (settings dialog).
     * Called when the user closes the dialog without going to settings.
     */
    fun dismissPermissionRationaleDialog() {
        _showPermissionRationaleDialog.update { false }
    }

    /**
     * Opens the app's settings page where the user can manually grant camera permission.
     * Called when the user taps the settings button in the rationale dialog.
     */
    fun openAppSettings() {
        _showPermissionRationaleDialog.update { false }
        permissionsController.openAppSettings()
    }

    /**
     * Handles a newly captured photo from the camera.
     * Stores the photo bytes and transitions to the preview screen.
     *
     * @param bytes The captured image as a byte array
     */
    fun onPhotoCaptured(bytes: ByteArray) {
        this.photoBytes = bytes
        _scanScreenState.update { ScanScreenState.CapturedImagePreview }
    }

    /**
     * Processes the captured photo (e.g., OCR, compression, upload).
     *
     * This function simulates a processing workflow:
     * 1. Shows processing screen
     * 2. Performs processing (currently simulated with delay)
     * 3. Shows success state briefly
     * 4. Closes the scan screen
     *
     * TODO: Replace simulation with actual image processing logic
     */
    fun processCapturedPhoto() {
        _scanScreenState.update { ScanScreenState.ProcessingImagePreview }
        viewModelScope.launch {
            delay(5000) // Simulate processing delay
            _scanScreenState.update { ScanScreenState.ImageProcessed("xyz-123456") }
        }
    }

    /**
     * Discards the current photo and returns to the camera preview.
     * Called when the user taps "Retake" on the preview screen.
     */
    fun retakePhoto() {
        this.photoBytes = null
        _scanScreenState.update { ScanScreenState.CameraPreview }
    }
}

/**
 * Represents the different states of the scan screen flow.
 *
 * The scan screen transitions through these states based on user actions and permission status:
 * Loading → CheckingPermission/CameraPreview/AlternateUploadScreen → CameraPreview →
 * CapturedImagePreview → ProcessingImagePreview → ImageProcessed → Close
 */
sealed class ScanScreenState {
    /** Initial loading state while checking camera availability */
    data object Loading : ScanScreenState()

    /** Showing permission request dialog to the user */
    data object CheckingPermission : ScanScreenState()

    /** Showing alternative upload options when camera is unavailable */
    data object AlternateUploadScreen : ScanScreenState()

    /** Displaying live camera preview for capturing receipts */
    data object CameraPreview : ScanScreenState()

    /** Showing preview of the captured image with Use/Retake options */
    data object CapturedImagePreview : ScanScreenState()

    /** Processing the captured image (OCR, compression, upload, etc.) */
    data object ProcessingImagePreview : ScanScreenState()

    /** Briefly showing success state after processing completes */
    data class ImageProcessed(val receiptId: String) : ScanScreenState()
}