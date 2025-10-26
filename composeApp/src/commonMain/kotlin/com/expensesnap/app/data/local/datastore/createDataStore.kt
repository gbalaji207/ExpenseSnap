package com.expensesnap.app.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

/**
 * Creates an instance of [DataStore] for storing [Preferences].
 *
 * @param producePath A lambda that produces the file path where the DataStore will be stored.
 * @return A [DataStore] instance for storing [Preferences].
 */
fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(produceFile = { producePath().toPath() })