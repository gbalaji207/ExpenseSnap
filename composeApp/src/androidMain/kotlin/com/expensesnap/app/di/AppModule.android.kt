package com.expensesnap.app.di

import android.app.Application
import com.expensesnap.app.data.local.datastore.createDataStore
import com.expensesnap.app.data.repository.AppPreferencesRepository
import dev.icerock.moko.permissions.PermissionsController
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { PermissionsController((androidContext() as Application).applicationContext) }

        single<AppPreferencesRepository> {
            AppPreferencesRepository(
                createDataStore(
                    (androidContext() as Application).applicationContext,
                    "datastore/app_preferences.preferences_pb"
                )
            )
        }
    }