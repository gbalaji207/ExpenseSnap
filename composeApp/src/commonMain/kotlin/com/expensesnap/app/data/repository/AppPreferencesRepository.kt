package com.expensesnap.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.expensesnap.app.data.local.datastore.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    val cameraPermissionRequested: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.CAMERA_PERMISSION_REQUESTED] ?: false
        }

    suspend fun setCameraPermissionRequested(requested: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CAMERA_PERMISSION_REQUESTED] = requested
        }
    }
}