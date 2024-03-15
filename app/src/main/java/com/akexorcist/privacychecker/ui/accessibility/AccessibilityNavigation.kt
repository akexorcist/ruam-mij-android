package com.akexorcist.privacychecker.ui.accessibility

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ACCESSIBILITY_ROUTE = "accessibility_route"

fun NavController.navigateToAccessibility(
    navOptions: NavOptions = NavOptions.Builder().build(),
) = navigate(ACCESSIBILITY_ROUTE, navOptions)

fun NavGraphBuilder.accessibilityScreen(
    navController: NavController,
) {
    composable(route = ACCESSIBILITY_ROUTE) {
        AccessibilityRoute(navController = navController)
    }
}
