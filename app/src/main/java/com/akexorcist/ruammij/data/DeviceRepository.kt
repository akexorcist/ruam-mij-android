package com.akexorcist.ruammij.data

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import com.akexorcist.ruammij.common.CoroutineDispatcherProvider
import com.akexorcist.ruammij.common.Installer
import com.akexorcist.ruammij.common.InstallerVerificationStatus
import com.akexorcist.ruammij.data.database.SafeApp
import com.akexorcist.ruammij.data.database.SafeAppDao
import com.akexorcist.ruammij.ui.overview.MediaProjectionApp
import com.akexorcist.ruammij.ui.overview.MediaProjectionState
import com.akexorcist.ruammij.utility.getInstaller
import com.akexorcist.ruammij.utility.getInstallerPackageName
import com.akexorcist.ruammij.utility.getOwnerPackageName
import com.akexorcist.ruammij.utility.getShaSignature
import com.akexorcist.ruammij.utility.toInstalledApp
import com.akexorcist.ruammij.utility.toInstaller
import kotlinx.coroutines.withContext
import kotlin.reflect.KMutableProperty0

interface DeviceRepository {
    suspend fun getInstalledApps(forceRefresh: Boolean = false): List<InstalledApp>

    suspend fun getSafeApps(forceRefresh: Boolean = false): List<SafeApp>

    suspend fun markAsSafe(packageName: String)

    suspend fun getInstalledApp(
        forceRefresh: Boolean = false,
        packageName: String,
    ): InstalledApp?

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
    private val safeAppDao: SafeAppDao,
) : DeviceRepository {
    private val packageManager: PackageManager
        get() = context.packageManager

    private val accessibilityManager: AccessibilityManager
        get() = context.getSystemService(AccessibilityManager::class.java)

    private val displayManager: DisplayManager
        get() = context.getSystemService(DisplayManager::class.java)

    private var cacheInstalledApps: List<InstalledApp>? = null

    override suspend fun getInstalledApps(forceRefresh: Boolean): List<InstalledApp> =
        getCachedDataOrFetch(::cacheInstalledApps, forceRefresh) {
            val safePackageNameList = getSafeApps(forceRefresh).map { it.packageName }
            val installedAppInfoList: Map<String, PackageInfo> = packageManager.getInstalledApplications(0)
                .mapNotNull {
                    runCatching { packageManager.getPackageInfo(it.packageName, 0) }.getOrNull()
                }
                .associateBy { it.packageName }

            val installers: Map<String?, Installer> = installedAppInfoList
                .map { (_, info) -> info.applicationInfo.getInstallerPackageName(packageManager) }
                .distinctBy { it }
                .map { installerPackageName ->
                    installedAppInfoList[installerPackageName]
                        .let { info ->
                            info.toInstaller(
                                packageName = installerPackageName,
                                packageManager = packageManager,
                            )
                        }
                }
                .associateBy { it.packageName }

            installedAppInfoList.map { (_, value) ->
                val installerPackageName = value.applicationInfo.getInstallerPackageName(packageManager)
                val installer = installers[installerPackageName]
                    ?: Installer(
                        name = when (installerPackageName == null) {
                            true -> "OS or ADB"
                            false -> null
                        },
                        packageName = installerPackageName,
                        verificationStatus = when (installerPackageName == null) {
                            true -> InstallerVerificationStatus.VERIFIED
                            false -> if (safePackageNameList.contains(installerPackageName)) {
                                InstallerVerificationStatus.VERIFIED
                            } else {
                                InstallerVerificationStatus.UNVERIFIED
                            }
                        },
                        sha256 = packageManager.getShaSignature(installerPackageName),
                    )
                value.toInstalledApp(packageManager, installer)
            }
        }

    override suspend fun getInstalledApp(
        forceRefresh: Boolean,
        packageName: String,
    ): InstalledApp? {
        val app = if (!forceRefresh) {
            cacheInstalledApps?.find { it.packageName == context.packageName }
        } else {
            null
        }
        return app ?: runCatching {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val installer = packageInfo.getInstaller(packageManager)
            packageInfo.toInstalledApp(packageManager, installer)
        }.getOrNull()
    }

    private var cacheSafeApps: List<SafeApp>? = null

    override suspend fun getSafeApps(forceRefresh: Boolean): List<SafeApp> =
        getCachedDataOrFetch(::cacheSafeApps, forceRefresh) { safeAppDao.getAll() }

    override suspend fun markAsSafe(packageName: String) = withContext(dispatcherProvider.io()) {
        safeAppDao.insert(SafeApp(packageName = packageName))
    }

    private var cacheEnabledAccessibilityApps: List<InstalledApp>? = null

    override suspend fun getEnabledAccessibilityApps(forceRefresh: Boolean): List<InstalledApp> =
        getCachedDataOrFetch(::cacheEnabledAccessibilityApps, forceRefresh) {
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
                .orEmpty()
                .mapNotNull { info ->
                    runCatching {
                        info.resolveInfo.serviceInfo.packageName.let {
                            packageManager.getPackageInfo(it, 0)
                        }
                    }.getOrNull()
                        ?.let { packageInfo ->
                            val installer = packageInfo.getInstaller(packageManager)
                            packageInfo.toInstalledApp(packageManager, installer)
                        }
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
                }.map { packageInfo ->
                    val installer = packageInfo.getInstaller(packageManager)
                    packageInfo.toInstalledApp(packageManager, installer)
                }
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
                runCatching {
                    val packageInfo = packageManager.getPackageInfo(packageName, 0)
                    val installer = packageInfo.getInstaller(packageManager)
                    packageInfo.toInstalledApp(packageManager, installer)
                }.getOrNull()
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
        Settings.Global.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0) == 1
    }

    override suspend fun isWirelessDebuggingEnabled(): Boolean? = withContext(dispatcherProvider.io()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Settings.Global.getInt(context.contentResolver, "adb_wifi_enabled", 0) == 1
        } else {
            null
        }
    }

    override suspend fun isDeveloperOptionsEnabled(): Boolean = withContext(dispatcherProvider.io()) {
        Settings.Global.getInt(context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1
    }

    private suspend inline fun <reified T> getCachedDataOrFetch(
        cacheProperty: KMutableProperty0<T?>,
        forceRefresh: Boolean,
        crossinline fetcher: suspend () -> T
    ): T =
        cacheProperty.takeIf { !forceRefresh }?.get()
            ?: withContext(dispatcherProvider.io()) { fetcher() }.also { cacheProperty.set(it) }
}
