package com.akexorcist.ruammij.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akexorcist.ruammij.AutoMediaProjectionDetectionEvent
import com.akexorcist.ruammij.common.Installers
import com.akexorcist.ruammij.data.DeviceRepository
import com.akexorcist.ruammij.data.InstalledApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OverviewViewModel(
    private val deviceRepository: DeviceRepository,
) : ViewModel() {

    private val _overviewUiState: MutableStateFlow<OverviewUiState> = MutableStateFlow(
        OverviewUiState.Loading
    )
    val overviewUiState = _overviewUiState.asStateFlow()

    fun checkDevicePrivacy() = viewModelScope.launch {
        _overviewUiState.update { OverviewUiState.Loading }
        val mediaProjectionApps = deviceRepository.getRunningMediaProjectionApps()
        val usbDebugging = deviceRepository.isUsbDebuggingEnabled()
        val wirelessDebugging = deviceRepository.isWirelessDebuggingEnabled()
        val developerOptions = deviceRepository.isDeveloperOptionsEnabled()
        val runningAccessibilityApps = deviceRepository.getEnabledAccessibilityApps()
        val unknownInstaller = deviceRepository.getInstalledApps().filterNot { app ->
            Installers.getVerifiedInstallers().contains(app.installer)
        }
        _overviewUiState.update {
            OverviewUiState.Complete(
                usbDebugging = usbDebugging,
                wirelessDebugging = wirelessDebugging,
                developerOptions = developerOptions,
                mediaProjectionApps = mediaProjectionApps,
                runningAccessibilityApps = runningAccessibilityApps,
                unknownInstaller = unknownInstaller,
            )
        }
    }

    fun updateMediaProjectionEventStatus(
        event: AutoMediaProjectionDetectionEvent,
    ) = viewModelScope.launch {
        val uiState = _overviewUiState.value as? OverviewUiState.Complete ?: return@launch

        val updateMediaProjectionApps = when (event) {
            is AutoMediaProjectionDetectionEvent.Detected -> {
                uiState.mediaProjectionApps.map { app ->
                    when (app.app.packageName == event.app.packageName) {
                        true -> app.copy(state = MediaProjectionState.DEACTIVATED)
                        false -> app
                    }
                } + MediaProjectionApp(
                    app = event.app,
                    state = MediaProjectionState.AUTO_DETECTED,
                    displayId = event.displayId,
                    updatedAt = System.currentTimeMillis(),
                )
            }

            is AutoMediaProjectionDetectionEvent.Deactivated -> {
                uiState.mediaProjectionApps.map { app ->
                    when (app.displayId == event.displayId) {
                        true -> app.copy(state = MediaProjectionState.DEACTIVATED)
                        false -> app
                    }
                }
            }
        }
        _overviewUiState.update { uiState.copy(mediaProjectionApps = updateMediaProjectionApps) }
    }
}

sealed class OverviewUiState {
    data object Loading : OverviewUiState()

    data class Complete(
        val usbDebugging: Boolean,
        val wirelessDebugging: Boolean?,
        val developerOptions: Boolean,
        val mediaProjectionApps: List<MediaProjectionApp>,
        val runningAccessibilityApps: List<InstalledApp>,
        val unknownInstaller: List<InstalledApp>,
    ) : OverviewUiState()
}

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
