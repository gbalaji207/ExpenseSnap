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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanViewModel(
    val permissionsController: PermissionsController,
    val appPreferencesRepository: AppPreferencesRepository
) : ViewModel() {

    private val _scanScreenState = MutableStateFlow<ScanScreenState>(ScanScreenState.Loading)
    val scanScreenState: StateFlow<ScanScreenState>
        get() = _scanScreenState

    private val _showPermissionRationaleDialog = MutableStateFlow(false)
    val showPermissionRationaleDialog: StateFlow<Boolean>
        get() = _showPermissionRationaleDialog

    var photoBytes: ByteArray? = null

    fun checkCameraPermission() {
        viewModelScope.launch {
            if (permissionsController.isPermissionGranted(Permission.CAMERA)) {
                _scanScreenState.update { ScanScreenState.CameraPreview }
            } else {
                val permissionAlreadyRequested =
                    appPreferencesRepository.cameraPermissionRequested.firstOrNull()
                if (permissionAlreadyRequested == true) {
                    _scanScreenState.update { ScanScreenState.AlternateUploadScreen }
                } else {
                    _scanScreenState.update { ScanScreenState.CheckingPermission }
                }
            }
        }
    }

    fun requestCameraPermission() {
        viewModelScope.launch {
            appPreferencesRepository.setCameraPermissionRequested(true)
            try {
                permissionsController.providePermission(Permission.CAMERA)
            } catch (_: DeniedAlwaysException) {
                _showPermissionRationaleDialog.value = true
            } catch (_: DeniedException) {
                _scanScreenState.update { ScanScreenState.AlternateUploadScreen }
            } finally {
                if (permissionsController.isPermissionGranted(Permission.CAMERA)) {
                    _scanScreenState.update { ScanScreenState.CameraPreview }
                }
            }
        }
    }

    fun cameraPermissionDialogDismissed() {
        _scanScreenState.update { ScanScreenState.AlternateUploadScreen }
    }

    fun dismissPermissionRationaleDialog() {
        _showPermissionRationaleDialog.value = false
    }

    fun openAppSettings() {
        _showPermissionRationaleDialog.value = false
        permissionsController.openAppSettings()
    }

    fun onPhotoCaptured(bytes: ByteArray) {
        this.photoBytes = bytes
        _scanScreenState.update { ScanScreenState.CapturedImagePreview }
    }

    fun processCapturedPhoto() {
        _scanScreenState.update { ScanScreenState.ProcessingImagePreview }
        viewModelScope.launch {
            delay(2000) // Simulate processing delay
            _scanScreenState.update { ScanScreenState.ImageProcessed }
            delay(1000) // Briefly show processed state
            _scanScreenState.update { ScanScreenState.Close }
        }
    }

    fun retakePhoto() {
        this.photoBytes = null
        _scanScreenState.update { ScanScreenState.CameraPreview }
    }
}

sealed class ScanScreenState {
    object Loading : ScanScreenState()
    object CheckingPermission : ScanScreenState()
    object AlternateUploadScreen : ScanScreenState()
    object CameraPreview : ScanScreenState()
    object CapturedImagePreview : ScanScreenState()
    object ProcessingImagePreview : ScanScreenState()
    object ImageProcessed : ScanScreenState()
    object Close : ScanScreenState()
}