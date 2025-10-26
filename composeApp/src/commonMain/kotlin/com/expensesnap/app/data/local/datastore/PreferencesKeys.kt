package com.expensesnap.app.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKeys {
    val CAMERA_PERMISSION_REQUESTED = booleanPreferencesKey("camera_permission_requested")
}