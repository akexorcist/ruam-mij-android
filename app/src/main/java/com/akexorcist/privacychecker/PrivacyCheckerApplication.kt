package com.akexorcist.privacychecker

import android.app.Application
import com.akexorcist.privacychecker.ui.AppModule
import org.koin.core.context.startKoin

class PrivacyCheckerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(AppModule.modules)
        }
    }
}
