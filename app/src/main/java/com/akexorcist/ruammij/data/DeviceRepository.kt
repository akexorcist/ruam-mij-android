package com.akexorcist.ruammij.data

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import com.akexorcist.ruammij.common.CoroutineDispatcherProvider
import com.akexorcist.ruammij.ui.overview.MediaProjectionApp
import com.akexorcist.ruammij.ui.overview.MediaProjectionState
import com.akexorcist.ruammij.utility.getOwnerPackageName
import com.akexorcist.ruammij.utility.toInstalledApp
import kotlinx.coroutines.withContext
import kotlin.reflect.*

interface DeviceRepository {
    suspend fun getInstalledApps(forceRefresh: Boolean = false): List<InstalledApp>

    suspend fun getEnabledAccessibilityApps(forceRefresh: Boolean = false): List<InstalledApp>

    suspend fun getAccessibilitySupportApps(forceRefresh: Boolean = false): List<InstalledApp>

    suspend fun getRunningMediaProjectionApps(forceRefresh: Boolean = false): List<MediaProjectionApp>

    suspend fun isUsbDebuggingEnabled(): Boolean

    suspend fun isWirelessDebuggingEnabled(): Boolean?

    suspend fun isDeveloperOptionsEnabled(): Boolean
}

class DefaultDeviceRepository(
    private val context: Context,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) : DeviceRepository {
    private val packageManager: PackageManager
        get() = context.packageManager

    private val accessibilityManager: AccessibilityManager
        get() = context.getSystemService(AccessibilityManager::class.java)

    private val displayManager: DisplayManager
        get() = context.getSystemService(DisplayManager::class.java)

    private var cacheInstalledApps: List<InstalledApp>? = null

    override suspend fun getInstalledApps(forceRefresh: Boolean): List<InstalledApp> =
        getCachedDataOrFetch(this::cacheInstalledApps, forceRefresh) {
            packageManager.getInstalledApplications(0).mapNotNull {
                runCatching {
                    packageManager.getPackageInfo(it.packageName, 0).toInstalledApp(packageManager)
                }.getOrNull()
            }
        }

    private var cacheEnabledAccessibilityApps: List<InstalledApp>? = null

    override suspend fun getEnabledAccessibilityApps(forceRefresh: Boolean): List<InstalledApp> =
        getCachedDataOrFetch(::cacheEnabledAccessibilityApps, forceRefresh) {
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
                .orEmpty()
                .mapNotNull { info ->
                    runCatching {
                        info.resolveInfo.serviceInfo.packageName.let { packageManager.getPackageInfo(it, 0) }
                            .toInstalledApp(packageManager)
                    }.getOrNull()
                }
        }

    private var cacheAccessibilitySupportApps: List<InstalledApp>? = null

    override suspend fun getAccessibilitySupportApps(forceRefresh: Boolean): List<InstalledApp> =
        getCachedDataOrFetch(::cacheAccessibilitySupportApps, forceRefresh) {
            packageManager.getInstalledPackages(PackageManager.GET_SERVICES)
                .filter { packageInfo ->
                    packageInfo.services
                        ?.any { serviceInfo -> serviceInfo.permission == Manifest.permission.BIND_ACCESSIBILITY_SERVICE }
                        ?: false
                }.map { it.toInstalledApp(packageManager) }
        }

    private var cacheRunningMediaProjectionApps: List<MediaProjectionApp>? = null

    override suspend fun getRunningMediaProjectionApps(forceRefresh: Boolean): List<MediaProjectionApp> =
        getCachedDataOrFetch(::cacheRunningMediaProjectionApps, forceRefresh) {
            (1 until 1000).asSequence().mapNotNull { displayId ->
                displayManager.getDisplay(displayId)
            }.mapNotNull { display ->
                display.getOwnerPackageName()?.let { packageName ->
                    display.displayId to packageName
                }
            }.mapNotNull { (displayId, packageName) ->
                runCatching { packageManager.getPackageInfo(packageName, 0).toInstalledApp(packageManager) }
                    .getOrNull()
                    ?.let { app -> displayId to app }
            }.map { (displayId, app) ->
                MediaProjectionApp(
                    app = app,
                    state = MediaProjectionState.MANUAL_DETECTED,
                    displayId = displayId,
                    updatedAt = System.currentTimeMillis(),
                )
            }.toList()
        }

    override suspend fun isUsbDebuggingEnabled(): Boolean = withContext(dispatcherProvider.io()) {
        Settings.Secure.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0) == 1
    }

    override suspend fun isWirelessDebuggingEnabled(): Boolean? = withContext(dispatcherProvider.io()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Settings.Secure.getInt(context.contentResolver, "adb_wifi_enabled", 0) == 1
        } else {
            null
        }
    }

    override suspend fun isDeveloperOptionsEnabled(): Boolean = withContext(dispatcherProvider.io()) {
        Settings.Secure.getInt(context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1
    }

    private suspend inline fun <reified T> getCachedDataOrFetch(
        cacheProperty: KMutableProperty0<T?>,
        forceRefresh: Boolean,
        crossinline fetcher: suspend () -> T
    ): T =
        cacheProperty.takeIf { !forceRefresh }?.get()
            ?: withContext(dispatcherProvider.io()) { fetcher() }.also { cacheProperty.set(it) }
}
