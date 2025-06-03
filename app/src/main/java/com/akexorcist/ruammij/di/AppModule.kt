package com.akexorcist.ruammij.di

import com.akexorcist.ruammij.functional.mediaprojection.MediaProjectionEventViewModel
import com.akexorcist.ruammij.base.common.CoroutineDispatcherProvider
import com.akexorcist.ruammij.base.common.DefaultCoroutineDispatcherProvider
import com.akexorcist.ruammij.functional.device.DefaultDeviceRepository
import com.akexorcist.ruammij.functional.device.DeviceRepository
import com.akexorcist.ruammij.feature.accessibility.AccessibilityViewModel
import com.akexorcist.ruammij.feature.installedapp.InstalledAppViewModel
import com.akexorcist.ruammij.feature.overview.OverviewViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object AppModule {
    private val modules = module {
        factory<CoroutineDispatcherProvider> { DefaultCoroutineDispatcherProvider() }
        single<DeviceRepository> { DefaultDeviceRepository(androidContext(), get(), get()) }
        viewModelOf(::OverviewViewModel)
        viewModelOf(::AccessibilityViewModel)
        viewModelOf(::InstalledAppViewModel)
        viewModelOf(::MediaProjectionEventViewModel)
    }

    val allModules = modules + databaseModule
}
