package com.akexorcist.ruammij.utility

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.akexorcist.ruammij.data.InstalledApp

fun PackageInfo.toInstalledApp(packageManager: PackageManager): InstalledApp {
    return InstalledApp(
        name = applicationInfo.loadLabel(packageManager).toString(),
        packageName = packageName,
        appVersion = getAppVersion(),
        icon = applicationInfo.loadIcon(packageManager),
        systemApp = applicationInfo.flags and 1 != 0,
        installedAt = firstInstallTime,
        installer = applicationInfo.getInstaller(packageManager),
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
fun ApplicationInfo.getInstaller(packageManager: PackageManager): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        packageManager.getInstallSourceInfo(packageName).installingPackageName
    } else {
        packageManager.getInstallerPackageName(packageName)
    }
}
