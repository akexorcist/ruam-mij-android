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
        val packageName: String?,
        val installer: String?,
        val showSystemApp: Boolean,
    )

    @Serializable
    data object Accessibility

    @Serializable
    data object OpenSourceLicense
}

fun BottomBarNavController.navigateToOverview() = with(controller) {
    navigate(route = Destinations.Overview) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

fun BottomBarNavController.navigateToAccessibility() = with(controller) {
    navigate(route = Destinations.Accessibility) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

fun BottomBarNavController.navigateToInstalledApp() = with(controller) {
    navigate(
        route = Destinations.InstalledApp(
            packageName = null,
            installer = null,
            showSystemApp = false,
        )
    ) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

fun BottomBarNavController.navigateToInstalledApp(
    installer: String?,
    showSystemApp: Boolean,
) = with(controller) {
    navigate(
        route = Destinations.InstalledApp(
            packageName = null,
            installer = installer,
            showSystemApp = showSystemApp,
        )
    ) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

fun BottomBarNavController.navigateToInstalledApp(
    packageName: String,
) = with(controller) {
    navigate(
        route = Destinations.InstalledApp(
            packageName = packageName,
            installer = null,
            showSystemApp = false,
        )
    ) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

fun BottomBarNavController.navigateToAboutApp() = with(controller) {
    navigate(route = Destinations.AboutApp) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

fun MainNavController.navigateToOpenSourceLicense() = with(controller) {
    navigate(route = Destinations.OpenSourceLicense) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}
