package com.akexorcist.ruammij

import android.hardware.display.DisplayManager
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import com.akexorcist.ruammij.ui.RUAM_MIJ_APP_ROUTE
import com.akexorcist.ruammij.ui.osslicense.openSourceLicenseScreen
import com.akexorcist.ruammij.ui.rememberAppState
import com.akexorcist.ruammij.ui.ruamMijApp
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import com.akexorcist.ruammij.base.utility.getOwnerPackageName
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : AppCompatActivity() {
    private val sharedEventViewModel: SharedEventViewModel by viewModel()

    private val displayManager: DisplayManager by lazy {
        getSystemService(DisplayManager::class.java)
    }

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = getColor(R.color.status_bar_light),
                darkScrim = getColor(R.color.status_bar_dark),
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = getColor(R.color.navigation_bar_light),
                darkScrim = getColor(R.color.navigation_bar_dark),
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            RuamMijTheme {
                val appState = rememberAppState()

                NavHost(
                    navController = appState.mainNavController,
                    startDestination = RUAM_MIJ_APP_ROUTE,
                ) {
                    ruamMijApp(appState = appState)
                    openSourceLicenseScreen(navController = appState.mainNavController)
                }
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
            sharedEventViewModel.onMediaProjectionDetected(packageName, displayId)
        }

        override fun onDisplayRemoved(displayId: Int) {
            sharedEventViewModel.onMediaProjectionDeactivated(displayId)
        }

        override fun onDisplayChanged(displayId: Int) = Unit
    }
}
