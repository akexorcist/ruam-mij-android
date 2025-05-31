package com.akexorcist.ruammij.ui.accessibility

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.akexorcist.ruammij.feature.accessibility.AccessibilityRoute
import com.akexorcist.ruammij.functional.core.navigation.Destinations

fun NavGraphBuilder.accessibilityScreen() {
    composable<Destinations.Accessibility> {
        AccessibilityRoute()
    }
}
