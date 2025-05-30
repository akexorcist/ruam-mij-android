package com.akexorcist.ruammij.data

enum class MediaProjectionState {
    MANUAL_DETECTED,
    AUTO_DETECTED,
    DEACTIVATED,
}

data class MediaProjectionApp(
    val app: InstalledApp,
    val state: MediaProjectionState,
    val displayId: Int,
    val updatedAt: Long,
)
