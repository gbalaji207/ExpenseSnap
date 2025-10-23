package com.expensesnap.app.di

import org.koin.dsl.module

val appModule = module {
    // Add your dependencies here as you build features
    // Example (you'll add these later):
    // single { ApiService(get()) }
    // single { ReceiptRepository(get(), get()) }
    // factory { HomeViewModel(get()) }
}