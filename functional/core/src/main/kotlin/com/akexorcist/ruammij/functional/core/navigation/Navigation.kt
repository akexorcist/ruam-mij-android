package com.akexorcist.ruammij.functional.core.navigation

import kotlinx.serialization.Serializable

object Destinations {
    @Serializable
    data object Root

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

fun BottomBarNavController.navigateToOverview() = with(controller) {
    navigate(Destinations.Overview) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

fun BottomBarNavController.navigateToAccessibility() = with(controller) {
    navigate(Destinations.Accessibility) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

fun BottomBarNavController.navigateToInstalledApp(
    installer: String? = null,
    showSystemApp: Boolean = false,
) = with(controller) {
    navigate(
        Destinations.InstalledApp(
            installer = installer,
            showSystemApp = showSystemApp,
        )
    ) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

fun BottomBarNavController.navigateToAboutApp() = with(controller) {
    navigate(Destinations.AboutApp) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

fun MainNavController.navigateToOpenSourceLicense() = with(controller) {
    navigate(Destinations.OpenSourceLicense) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}
