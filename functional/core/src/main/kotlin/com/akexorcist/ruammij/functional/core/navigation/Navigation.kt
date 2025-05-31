package com.akexorcist.ruammij.functional.core.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

object Destinations {
    @Serializable
    data object Overview

    @Serializable
    data object AboutApp

    @Serializable
    data class InstalledApp(
        val installer: String?,
        val showSystemApp: Boolean
    )

    @Serializable
    data object Accessibility

    @Serializable
    data object OpenSourceLicense
}

fun NavController.navigateToOverview() = navigate(Destinations.Overview) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavController.navigateToAccessibility() = navigate(Destinations.Accessibility) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavController.navigateToInstalledApp(
    installer: String? = null,
    showSystemApp: Boolean = false,
) = navigate(
    Destinations.InstalledApp(
        installer = installer,
        showSystemApp = showSystemApp,
    )
) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavController.navigateToAboutApp() = navigate(Destinations.AboutApp) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}

fun NavController.navigateToOpenSourceLicense() = navigate(Destinations.OpenSourceLicense) {
    popUpTo(graph.startDestinationId)
    launchSingleTop = true
}
