package com.akexorcist.ruammij.ui.accessibility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akexorcist.ruammij.data.DeviceRepository
import com.akexorcist.ruammij.data.InstalledApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccessibilityViewModel(
    private val deviceRepository: DeviceRepository,
) : ViewModel() {
    private val _accessibilityUiState: MutableStateFlow<AccessibilityUiState> = MutableStateFlow(
        AccessibilityUiState.Loading
    )
    val accessibilityUiState = _accessibilityUiState.asStateFlow()

    fun loadAccessibilityApps() = viewModelScope.launch {
        _accessibilityUiState.update { AccessibilityUiState.Loading }
        val activeAccessibilityApps = deviceRepository.getEnabledAccessibilityApps()
        val accessibilitySupportApps = deviceRepository.getAccessibilitySupportApps()
        val inactiveAccessibilityApps = accessibilitySupportApps.filterNot { app -> activeAccessibilityApps.any { it.packageName == app.packageName } }
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
