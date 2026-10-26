package com.akexorcist.ruammij

import com.akexorcist.ruammij.base.data.InstalledApp
import com.akexorcist.ruammij.base.data.Installer
import com.akexorcist.ruammij.base.data.InstallerVerificationStatus
import com.akexorcist.ruammij.fake.FakeDeviceRepository
import com.akexorcist.ruammij.feature.installedapp.DisplayOption
import com.akexorcist.ruammij.feature.installedapp.InstalledAppUiState
import com.akexorcist.ruammij.feature.installedapp.InstalledAppViewModel
import com.akexorcist.ruammij.feature.installedapp.SortBy
import com.akexorcist.ruammij.feature.overview.OverviewUiState
import com.akexorcist.ruammij.feature.overview.OverviewViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrustViewModelTest {
    private class Repository : FakeDeviceRepository() {
        var failTrust = false
        val trusted = mutableSetOf<String>()
        private val installer = Installer("Source", "test.source", InstallerVerificationStatus.UNVERIFIED, "cert")
        private val apps = listOf("test.app", "test.sibling").map { name ->
            InstalledApp(name, name, "1", null, false, 0L, installer, "cert")
        }
        override suspend fun getInstalledApps(forceRefresh: Boolean) =
            apps.map { it.copy(isTrustedByUser = it.packageName in trusted) }
        override suspend fun markAsSafe(packageName: String) {
            check(!failTrust)
            trusted.add(packageName)
        }
        override suspend fun unmarkAsSafe(packageName: String) {
            trusted.remove(packageName)
        }
    }

    @Before fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())
    @After fun tearDown() = Dispatchers.resetMain()

    @Test fun toggleTrustPreservesFiltersAndUpdatesTheSelectedApp() = runTest {
        val repository = Repository()
        val viewModel = InstalledAppViewModel(repository)
        viewModel.loadInstalledApps(null, false).join()
        val option = DisplayOption(SortBy.PackageName, true, true, listOf("test.source"))
        viewModel.updateDisplayOption(option)
        viewModel.toggleUserTrust("test.app").join()
        var state = viewModel.installedAppUiState.value as InstalledAppUiState.InstalledAppLoaded
        assertEquals(option, state.displayOption)
        assertTrue(state.installedApps.first { it.packageName == "test.app" }.isTrustedByUser)
        assertFalse(state.installedApps.first { it.packageName == "test.sibling" }.isTrustedByUser)
        viewModel.toggleUserTrust("test.app").join()
        state = viewModel.installedAppUiState.value as InstalledAppUiState.InstalledAppLoaded
        assertEquals(option, state.displayOption)
        assertFalse(state.installedApps.first { it.packageName == "test.app" }.isTrustedByUser)
    }

    @Test fun failedTrustDoesNotChangeAppStateAndCanBeDismissed() = runTest {
        val repository = Repository().apply { failTrust = true }
        val viewModel = InstalledAppViewModel(repository)
        viewModel.loadInstalledApps(null, false).join()
        val before = viewModel.installedAppUiState.value
        viewModel.toggleUserTrust("test.app").join()
        assertEquals(before, viewModel.installedAppUiState.value)
        assertTrue(viewModel.trustUpdateFailed.value)
        viewModel.dismissTrustUpdateError()
        assertFalse(viewModel.trustUpdateFailed.value)
    }

    @Test fun overviewExcludesOnlyTheAppExplicitlyTrustedByUser() = runTest {
        val repository = Repository()
        val viewModel = OverviewViewModel(repository)
        repository.markAsSafe("test.app")
        viewModel.checkDevicePrivacy().join()
        val state = viewModel.overviewUiState.value as OverviewUiState.Complete
        assertEquals(listOf("test.sibling"), state.unknownInstaller.map { it.packageName })
        repository.unmarkAsSafe("test.app")
        viewModel.checkDevicePrivacy(forceRefresh = true).join()
        assertEquals(2, (viewModel.overviewUiState.value as OverviewUiState.Complete).unknownInstaller.size)
    }
}
