package com.akexorcist.ruammij.di

import com.akexorcist.ruammij.SharedEventViewModel
import com.akexorcist.ruammij.base.common.CoroutineDispatcherProvider
import com.akexorcist.ruammij.base.common.DefaultCoroutineDispatcherProvider
import com.akexorcist.ruammij.functional.device.DefaultDeviceRepository
import com.akexorcist.ruammij.functional.device.DeviceRepository
import com.akexorcist.ruammij.ui.accessibility.AccessibilityViewModel
import com.akexorcist.ruammij.ui.installedapp.InstalledAppViewModel
import com.akexorcist.ruammij.ui.overview.OverviewViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object AppModule {
    private val modules = module {
        factory<CoroutineDispatcherProvider> { DefaultCoroutineDispatcherProvider() }
        single<DeviceRepository> { DefaultDeviceRepository(androidContext(), get(), get()) }
        viewModelOf(::OverviewViewModel)
        viewModelOf(::AccessibilityViewModel)
        viewModelOf(::InstalledAppViewModel)
        viewModelOf(::SharedEventViewModel)
    }

    val allModules = modules + databaseModule
}
