package com.akexorcist.ruammij

import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.akexorcist.ruammij.data.InstalledApp
import com.akexorcist.ruammij.ui.RuamMijApp
import com.akexorcist.ruammij.ui.rememberAppState
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import com.akexorcist.ruammij.utility.getOwnerPackageName
import com.akexorcist.ruammij.utility.toInstalledApp
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val sharedEventViewModel: SharedEventViewModel by viewModel()

    private val displayManager: DisplayManager by lazy {
        getSystemService(DisplayManager::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            RuamMijTheme {
                val appState = rememberAppState()
                RuamMijApp(appState = appState)
            }
        }
        displayManager.registerDisplayListener(displayListener, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        displayManager.unregisterDisplayListener(displayListener)
    }

    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) {
            val display = displayManager.getDisplay(displayId) ?: return
            val packageName = display.getOwnerPackageName() ?: return
            val app = getInstalledApp(packageName) ?: return
            sharedEventViewModel.onMediaProjectionDetected(app, displayId)
        }

        override fun onDisplayRemoved(displayId: Int) {
            sharedEventViewModel.onMediaProjectionDeactivated(displayId)
        }

        override fun onDisplayChanged(displayId: Int) = Unit
    }

    private fun getInstalledApp(packageName: String): InstalledApp? {
        return try {
            packageManager.getPackageInfo(packageName, 0).toInstalledApp(packageManager)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}
