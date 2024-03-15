package com.akexorcist.privacychecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.akexorcist.privacychecker.ui.PrivacyCheckerApp
import com.akexorcist.privacychecker.ui.rememberAppState
import com.akexorcist.privacychecker.ui.theme.PrivacyCheckerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrivacyCheckerTheme {
                val appState = rememberAppState()
                PrivacyCheckerApp(appState = appState)
            }
        }
    }
}
