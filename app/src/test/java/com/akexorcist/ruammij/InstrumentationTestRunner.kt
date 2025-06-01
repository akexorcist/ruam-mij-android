package com.akexorcist.ruammij

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.akexorcist.ruammij.di.AppModule
import com.akexorcist.ruammij.fake.FakeDeviceRepository
import com.akexorcist.ruammij.functional.device.DeviceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class InstrumentationTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(classLoader, TestRuamMijApplication::class.java.name, context)
    }
}

private val modulesOverride = module {
    single<DeviceRepository> {
        FakeDeviceRepository()
    }
}

class TestRuamMijApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TestRuamMijApplication)
            modules(AppModule.allModules)
            modules(modulesOverride)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}
