package com.expensesnap.app

import androidx.compose.ui.window.ComposeUIViewController
import com.expensesnap.app.di.appModule
import com.expensesnap.app.di.platformModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin
    startKoin {
        modules(appModule)
        modules(platformModule)
    }

    App()
}