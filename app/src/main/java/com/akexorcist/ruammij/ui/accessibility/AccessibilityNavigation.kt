package com.akexorcist.ruammij.ui.accessibility

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val ACCESSIBILITY_ROUTE = "accessibility_route"

fun NavController.navigateToAccessibility() = navigate(ACCESSIBILITY_ROUTE) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavGraphBuilder.accessibilityScreen() {
    composable(route = ACCESSIBILITY_ROUTE) {
        AccessibilityRoute()
    }
}
