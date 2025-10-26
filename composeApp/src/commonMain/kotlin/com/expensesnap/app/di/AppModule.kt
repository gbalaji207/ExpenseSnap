package com.expensesnap.app.di

import com.expensesnap.app.ui.scan.ScanViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    viewModelOf(::ScanViewModel)
}