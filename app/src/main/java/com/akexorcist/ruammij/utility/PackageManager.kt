package com.akexorcist.ruammij.utility

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.akexorcist.ruammij.common.Installer
import com.akexorcist.ruammij.common.InstallerVerificationStatus
import com.akexorcist.ruammij.common.Installers
import com.akexorcist.ruammij.common.ReferenceInstallerStatus
import com.akexorcist.ruammij.data.AdditionalInfo
import com.akexorcist.ruammij.data.InstalledApp
import java.security.MessageDigest

private const val ALGORITHM_SHA_512 = "SHA-256"
private val ANDROID_PERMISSION_PREFIX = setOf("android.permission.", "com.google.android.")

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
        sha256 = packageManager.getShaSignature(packageName),
        permissions = packageManager.getPermissions(requestedPermissions),
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
        val systemApp = applicationInfo.flags and 1 != 0
        val sha256 = packageManager.getShaSignature(info.packageName)
        Installer(
            name = info.applicationInfo.loadLabel(packageManager).toString(),
            packageName = packageName,
            verificationStatus = Installers.apps[info.packageName]?.let { status ->
                when (status) {
                    is ReferenceInstallerStatus.Verified -> when {
                        systemApp -> InstallerVerificationStatus.VERIFIED
                        status.sha256 == null -> InstallerVerificationStatus.VERIFIED
                        sha256 == status.sha256 -> InstallerVerificationStatus.VERIFIED
                        else -> InstallerVerificationStatus.UNVERIFIED
                    }

                    is ReferenceInstallerStatus.SideLoad -> InstallerVerificationStatus.SIDE_LOAD
                    is ReferenceInstallerStatus.Unverified -> InstallerVerificationStatus.UNVERIFIED
                }
            } ?: InstallerVerificationStatus.UNVERIFIED,
            sha256 = sha256,
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
        sha256 = packageManager.getShaSignature(packageName),
    )
}

fun PackageManager.getShaSignature(packageName: String?): String {
    packageName ?: return ""
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        runCatching {
            this.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        }.getOrNull()?.let { info ->
            info.signingInfo?.apkContentsSigners?.firstNotNullOf { signature ->
                runCatching { MessageDigest.getInstance(ALGORITHM_SHA_512) }
                    .getOrNull()
                    ?.let { signature to it }
            }?.let { (signature, messageDigest) ->
                messageDigest.update(signature.toByteArray())
                messageDigest.digest()
            }?.let { digest ->
                digest
                    .fold("") { acc, value -> acc + "%02x".format(value).uppercase() }
                    .chunked(2)
                    .joinToString(separator = ":")
            }
        }
    } else {
        runCatching {
            @Suppress("DEPRECATION")
            this.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        }.getOrNull()?.let { info ->
            @Suppress("DEPRECATION")
            info.signatures?.firstNotNullOf { signature ->
                runCatching { MessageDigest.getInstance(ALGORITHM_SHA_512) }
                    .getOrNull()
                    ?.let { signature to it }
            }?.let { (signature, messageDigest) ->
                messageDigest.update(signature.toByteArray())
                messageDigest.digest()
            }?.let { digest ->
                digest
                    .fold("") { acc, value -> acc + "%02x".format(value).uppercase() }
                    .chunked(2)
                    .joinToString(separator = ":")
            }
        }
    } ?: ""
}

fun PackageManager.getPermissions(permissions: Array<String>?): List<AdditionalInfo> {
    permissions ?: return emptyList()

    return permissions
        .mapNotNull {
            runCatching { getPermissionInfo(it, PackageManager.GET_META_DATA) }.getOrNull()
        }
        .filter { ANDROID_PERMISSION_PREFIX.any { prefix -> it.name.startsWith(prefix) } }
        .map { permission ->
            AdditionalInfo(
                name = permission.name,
                label = permission.loadLabel(this).toString()
                    .replaceFirstChar { it.uppercase() },
                description = permission.loadDescription(this).toString()
            )
        }
}

