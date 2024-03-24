package com.akexorcist.ruammij.di

import com.akexorcist.ruammij.SharedEventViewModel
import com.akexorcist.ruammij.common.CoroutineDispatcherProvider
import com.akexorcist.ruammij.common.DefaultCoroutineDispatcherProvider
import com.akexorcist.ruammij.data.DefaultDeviceRepository
import com.akexorcist.ruammij.data.DeviceRepository
import com.akexorcist.ruammij.ui.accessibility.AccessibilityViewModel
import com.akexorcist.ruammij.ui.installedapp.InstalledAppViewModel
import com.akexorcist.ruammij.ui.overview.OverviewViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object AppModule {
    val modules = module {
        factory<CoroutineDispatcherProvider> { DefaultCoroutineDispatcherProvider() }
        single<DeviceRepository> { DefaultDeviceRepository(androidContext(), get(), get()) }
        viewModelOf(::OverviewViewModel)
        viewModelOf(::AccessibilityViewModel)
        viewModelOf(::InstalledAppViewModel)
        viewModelOf(::SharedEventViewModel)
    }
    val databaseModule = module {
        single { provideDataBase(get()) }
        single { provideSafeAppDao(get()) }
    }
}
