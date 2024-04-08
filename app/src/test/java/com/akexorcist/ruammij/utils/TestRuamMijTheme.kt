package com.akexorcist.ruammij.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import org.koin.androidx.compose.KoinAndroidContext

@Composable
fun TestRuamMijTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    KoinAndroidContext {
        RuamMijTheme(darkTheme, content)
    }
}
