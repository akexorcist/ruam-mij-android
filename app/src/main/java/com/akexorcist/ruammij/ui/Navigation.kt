package com.akexorcist.ruammij.ui

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
