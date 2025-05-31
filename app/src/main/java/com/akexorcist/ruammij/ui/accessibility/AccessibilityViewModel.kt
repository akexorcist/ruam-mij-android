package com.akexorcist.ruammij.ui.accessibility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akexorcist.ruammij.base.data.InstalledApp
import com.akexorcist.ruammij.functional.device.DeviceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AccessibilityViewModel(
    private val deviceRepository: DeviceRepository,
) : ViewModel() {
    private val _accessibilityUiState: MutableStateFlow<AccessibilityUiState> = MutableStateFlow(
        AccessibilityUiState.Loading
    )
    val accessibilityUiState = _accessibilityUiState.asStateFlow()

    fun loadAccessibilityApps(forceRefresh: Boolean = false) = viewModelScope.launch {
        _accessibilityUiState.update { AccessibilityUiState.Loading }
        val activeAccessibilityAppsDeferred = async { deviceRepository.getEnabledAccessibilityApps(forceRefresh) }
        val accessibilitySupportAppsDeferred = async { deviceRepository.getAccessibilitySupportApps(forceRefresh) }

        val activeAccessibilityApps = activeAccessibilityAppsDeferred.await()
        val accessibilitySupportApps = accessibilitySupportAppsDeferred.await()
        val inactiveAccessibilityApps = accessibilitySupportApps
            .filterNot { app -> activeAccessibilityApps.any { it.packageName == app.packageName } }

        _accessibilityUiState.update {
            AccessibilityUiState.AccessibilityAppLoaded(
                active = activeAccessibilityApps,
                inactive = inactiveAccessibilityApps,
            )
        }
    }
}

sealed class AccessibilityUiState {
    data object Loading : AccessibilityUiState()

    data class AccessibilityAppLoaded(
        val active: List<InstalledApp>,
        val inactive: List<InstalledApp>,
    ) : AccessibilityUiState()
}
