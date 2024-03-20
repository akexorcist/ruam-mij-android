package com.akexorcist.ruammij.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akexorcist.ruammij.AutoMediaProjectionDetectionEvent
import com.akexorcist.ruammij.common.*
import com.akexorcist.ruammij.data.DeviceRepository
import com.akexorcist.ruammij.data.InstalledApp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.time.*
import kotlin.system.*

class OverviewViewModel(
    private val deviceRepository: DeviceRepository,
) : ViewModel() {

    private val _overviewUiState: MutableStateFlow<OverviewUiState> = MutableStateFlow(
        OverviewUiState.Loading
    )
    val overviewUiState = _overviewUiState.asStateFlow()

    fun checkDevicePrivacy(forceRefresh: Boolean = false) = viewModelScope.launch {
        _overviewUiState.update { OverviewUiState.Loading }
        val usbDebugging = deviceRepository.isUsbDebuggingEnabled()
        val wirelessDebugging = deviceRepository.isWirelessDebuggingEnabled()
        val developerOptions = deviceRepository.isDeveloperOptionsEnabled()


        val mediaProjectionAppsDeferred = async {
            deviceRepository.getRunningMediaProjectionApps(forceRefresh)
        }

        val runningAccessibilityAppsDeferred = async {
            deviceRepository.getEnabledAccessibilityApps(forceRefresh)
        }
        val unknownInstallerDeferred = async {
            deviceRepository.getInstalledApps(forceRefresh).filter {
                Installer.fromPackageName(it.installer)?.verificationStatus != InstallerVerificationStatus.VERIFIED
            }
        }
        _overviewUiState.update {
            OverviewUiState.Complete(
                usbDebugging = usbDebugging,
                wirelessDebugging = wirelessDebugging,
                developerOptions = developerOptions,
                mediaProjectionApps = mediaProjectionAppsDeferred.await(),
                runningAccessibilityApps = runningAccessibilityAppsDeferred.await(),
                unknownInstaller = unknownInstallerDeferred.await(),
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
