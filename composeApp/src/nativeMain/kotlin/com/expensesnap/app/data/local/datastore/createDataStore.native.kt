package com.expensesnap.app.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

fun createDataStore(path: String): DataStore<Preferences> = createDataStore(
    producePath = {
        val documentDirectory: NSURL? = getDocumentDirectory()
        requireNotNull(documentDirectory).path + "/$path"
    }
)

@OptIn(ExperimentalForeignApi::class)
private fun getDocumentDirectory(): NSURL {
    return NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )!!
}