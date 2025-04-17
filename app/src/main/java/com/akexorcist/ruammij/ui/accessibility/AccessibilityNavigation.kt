package com.akexorcist.ruammij.ui.accessibility

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.akexorcist.ruammij.ui.Destinations

fun NavController.navigateToAccessibility() = navigate(Destinations.Accessibility) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavGraphBuilder.accessibilityScreen() {
    composable<Destinations.Accessibility> {
        AccessibilityRoute()
    }
}
