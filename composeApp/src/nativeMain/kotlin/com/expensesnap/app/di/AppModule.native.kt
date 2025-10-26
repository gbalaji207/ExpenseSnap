package com.expensesnap.app.di

import com.expensesnap.app.data.local.datastore.createDataStore
import com.expensesnap.app.data.repository.AppPreferencesRepository
import dev.icerock.moko.permissions.ios.PermissionsController
import org.koin.dsl.module

actual val platformModule: org.koin.core.module.Module
    get() = module {
        single { PermissionsController() }

        single<AppPreferencesRepository> { AppPreferencesRepository(createDataStore("datastore/app_preferences.preferences_pb")) }
    }