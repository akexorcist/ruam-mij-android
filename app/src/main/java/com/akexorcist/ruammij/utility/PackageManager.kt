package com.akexorcist.ruammij.utility

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.akexorcist.ruammij.common.Installer
import com.akexorcist.ruammij.common.InstallerVerificationStatus
import com.akexorcist.ruammij.common.Installers
import com.akexorcist.ruammij.data.InstalledApp

fun PackageInfo.toInstalledApp(
    packageManager: PackageManager,
    installer: Installer,
): InstalledApp {
    return InstalledApp(
        name = applicationInfo.loadLabel(packageManager).toString(),
        packageName = packageName,
        appVersion = getAppVersion(),
        icon = applicationInfo.loadIcon(packageManager),
        systemApp = applicationInfo.flags and 1 != 0,
        installedAt = firstInstallTime,
        installer = installer,
    )
}

@Suppress("DEPRECATION")
fun PackageInfo.getAppVersion(): String {
    val versionCode =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) longVersionCode
        else versionCode.toLong()
    return "${versionName ?: "N/A"} ($versionCode)"
}

@Suppress("DEPRECATION")
fun ApplicationInfo.getInstallerPackageName(packageManager: PackageManager): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        packageManager.getInstallSourceInfo(packageName).installingPackageName
    } else {
        packageManager.getInstallerPackageName(packageName)
    }
}

fun PackageInfo.getInstaller(packageManager: PackageManager): Installer {
    val installerPackageName = this.applicationInfo.getInstallerPackageName(packageManager)
    return runCatching {
        installerPackageName?.let { packageManager.getPackageInfo(it, 0) }
    }.getOrNull().let { installerPackageInfo ->
        installerPackageInfo.toInstaller(installerPackageName, packageManager)
    }
}

fun PackageInfo?.toInstaller(
    packageName: String?,
    packageManager: PackageManager,
): Installer {
    return this?.let { info ->
        Installer(
            name = info.applicationInfo.loadLabel(packageManager).toString(),
            packageName = packageName,
            verificationStatus = Installers.apps[info.packageName] ?: InstallerVerificationStatus.UNVERIFIED
        )
    } ?: Installer(
        name = when (packageName == null) {
            true -> "OS or ADB"
            false -> null
        },
        packageName = packageName,
        verificationStatus = when (packageName == null) {
            true -> InstallerVerificationStatus.VERIFIED
            false -> InstallerVerificationStatus.UNVERIFIED
        },
    )
}
