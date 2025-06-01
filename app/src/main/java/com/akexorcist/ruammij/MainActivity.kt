package com.akexorcist.ruammij

import android.hardware.display.DisplayManager
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import com.akexorcist.ruammij.ui.ruamMijApp
import com.akexorcist.ruammij.base.ui.theme.RuamMijTheme
import com.akexorcist.ruammij.base.utility.getOwnerPackageName
import com.akexorcist.ruammij.functional.core.navigation.Destinations
import com.akexorcist.ruammij.functional.core.state.rememberAppState
import com.akexorcist.ruammij.functional.mediaprojection.MediaProjectionEventViewModel
import com.akexorcist.ruammij.ui.openSourceLicenseScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : AppCompatActivity() {
    private val mediaProjectionEventViewModel: MediaProjectionEventViewModel by viewModel()

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
                val mainNavController = appState.mainNavController.controller
                NavHost(
                    navController = mainNavController,
                    startDestination = Destinations.Root,
                ) {
                    ruamMijApp(appState = appState)
                    openSourceLicenseScreen(navController = mainNavController)
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
            mediaProjectionEventViewModel.onMediaProjectionDetected(packageName, displayId)
        }

        override fun onDisplayRemoved(displayId: Int) {
            mediaProjectionEventViewModel.onMediaProjectionDeactivated(displayId)
        }

        override fun onDisplayChanged(displayId: Int) = Unit
    }
}
