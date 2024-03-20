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

interface DeviceRepository {
    suspend fun getInstalledApps(): List<InstalledApp>

    suspend fun getEnabledAccessibilityApps(): List<InstalledApp>

    suspend fun getAccessibilitySupportApps(): List<InstalledApp>

    suspend fun isUsbDebuggingEnabled(): Boolean

    suspend fun isWirelessDebuggingEnabled(): Boolean?

    suspend fun isDeveloperOptionsEnabled(): Boolean

    suspend fun getRunningMediaProjectionApps(): List<MediaProjectionApp>
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

    override suspend fun getInstalledApps(): List<InstalledApp> = withContext(dispatcherProvider.io()) {
        packageManager.getInstalledApplications(0).mapNotNull {
            try {
                packageManager.getPackageInfo(it.packageName, 0).toInstalledApp(packageManager)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun getEnabledAccessibilityApps(): List<InstalledApp> = withContext(dispatcherProvider.io()) {
        accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            ?.mapNotNull {
                val packageName = it.resolveInfo.serviceInfo.packageName
                try {
                    packageManager.getPackageInfo(packageName, 0).toInstalledApp(packageManager)
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                    null
                }
            }
            ?: listOf()
    }

    override suspend fun getAccessibilitySupportApps(): List<InstalledApp> = withContext(dispatcherProvider.io()) {
        packageManager.getInstalledPackages(PackageManager.GET_SERVICES)
            .filter { packageInfo ->
                packageInfo.services
                    ?.any { serviceInfo -> serviceInfo.permission == Manifest.permission.BIND_ACCESSIBILITY_SERVICE }
                    ?: false
            }.map { it.toInstalledApp(packageManager) }
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

    override suspend fun getRunningMediaProjectionApps(): List<MediaProjectionApp> = withContext(dispatcherProvider.io()) {
        (1 until 1000).mapNotNull { displayId ->
            displayManager.getDisplay(displayId)
        }.mapNotNull { display ->
            display.getOwnerPackageName()?.let { packageName ->
                display.displayId to packageName
            }
        }.mapNotNull { (displayId, packageName) ->
            try {
                packageManager.getPackageInfo(packageName, 0).toInstalledApp(packageManager)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                null
            }?.let { app -> displayId to app }
        }.map { (displayId, app) ->
            MediaProjectionApp(
                app = app,
                state = MediaProjectionState.MANUAL_DETECTED,
                displayId = displayId,
                updatedAt = System.currentTimeMillis(),
            )
        }
    }
}
