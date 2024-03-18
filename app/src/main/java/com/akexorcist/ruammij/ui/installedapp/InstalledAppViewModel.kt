package com.akexorcist.ruammij.ui.installedapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akexorcist.ruammij.common.Installers
import com.akexorcist.ruammij.data.DeviceRepository
import com.akexorcist.ruammij.data.InstalledApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

class InstalledAppViewModel(
    private val deviceRepository: DeviceRepository,
) : ViewModel() {
    private val _installedAppUiState: MutableStateFlow<InstalledAppUiState> = MutableStateFlow(
        InstalledAppUiState.Loading(DisplayOption.default)
    )
    val installedAppUiState = _installedAppUiState.asStateFlow()

    fun loadInstalledApps(
        preferredInstaller: String?,
    ) = viewModelScope.launch {
        _installedAppUiState.update { InstalledAppUiState.Loading(it.displayOption) }
        val installedApps = deviceRepository.getInstalledApps()
        _installedAppUiState.update {
            val installers = installedApps
                .distinctBy { app -> app.installer }
                .map { app -> app.installer }
                .sortedBy { installer -> installer }
            val displayOption = when (preferredInstaller != null) {
                true -> it.displayOption.copy(installers = listOf(preferredInstaller))
                false -> it.displayOption.copy(installers = installers)
            }
            InstalledAppUiState.InstalledAppLoaded(
                displayOption = displayOption,
                installedApps = installedApps,
                installers = installers,
                displayInstalledApps = installedApps.applyDisplayOption(displayOption)
            )
        }
    }

    fun updateDisplayOption(displayOption: DisplayOption) {
        _installedAppUiState.update {
            when (it) {
                is InstalledAppUiState.Loading -> it.copy(displayOption = displayOption)
                is InstalledAppUiState.InstalledAppLoaded -> it.copy(
                    displayOption = displayOption,
                    displayInstalledApps = it.installedApps.applyDisplayOption(displayOption)
                )
            }
        }
    }

    private fun List<InstalledApp>.applyDisplayOption(displayOption: DisplayOption): List<InstalledApp> {
        return when {
            displayOption.showSystemApp -> this
            else -> this.filterNot { it.systemApp }
        }.let { apps ->
            when {
                displayOption.hideVerifiedInstaller -> apps.filterNot { isVerifiedInstaller(it.installer) }
                displayOption.installers.isNotEmpty() -> apps.filter { displayOption.installers.contains(it.installer) }
                else -> apps
            }
        }.sortedBy {
            when (displayOption.sortBy) {
                SortBy.Name -> it.name
                SortBy.PackageName -> it.packageName
                SortBy.InstalledDate -> Instant.ofEpochMilli(it.installedAt ?: 0L).toString()
                SortBy.Installer -> it.installer
            }
        }
    }

    private fun isVerifiedInstaller(installer: String?): Boolean = Installers.getVerifiedInstallers().contains(installer)
}

sealed class InstalledAppUiState(
    open val displayOption: DisplayOption,
) {
    data class Loading(override val displayOption: DisplayOption) : InstalledAppUiState(displayOption)

    data class InstalledAppLoaded(
        override val displayOption: DisplayOption,
        val installedApps: List<InstalledApp>,
        val displayInstalledApps: List<InstalledApp>,
        val installers: List<String?>,
    ) : InstalledAppUiState(displayOption)
}

data class DisplayOption(
    val sortBy: SortBy,
    val showSystemApp: Boolean,
    val hideVerifiedInstaller: Boolean,
    val installers: List<String?>,
) {
    companion object {
        val default = DisplayOption(
            sortBy = SortBy.Name,
            showSystemApp = false,
            hideVerifiedInstaller = false,
            installers = listOf(),
        )
    }
}

enum class SortBy {
    Name,
    PackageName,
    InstalledDate,
    Installer,
}