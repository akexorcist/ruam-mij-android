package com.akexorcist.ruammij.common


enum class InstallerVerificationStatus {
    VERIFIED,
    UNVERIFIED,
    SIDE_LOAD,
}

object Installers {
    val apps: Map<String?, InstallerVerificationStatus> = mapOf(
        null to InstallerVerificationStatus.VERIFIED,
        "android" to InstallerVerificationStatus.VERIFIED,
        "com.android.vending" to InstallerVerificationStatus.VERIFIED,
        "com.amazon.venezia" to InstallerVerificationStatus.VERIFIED,
        "com.heytap.market" to InstallerVerificationStatus.VERIFIED,
        "com.vivo.appstore" to InstallerVerificationStatus.VERIFIED,
        "com.bbk.appstore" to InstallerVerificationStatus.VERIFIED,
        "com.bbk.launcher2" to InstallerVerificationStatus.VERIFIED,
        "com.bbk.theme" to InstallerVerificationStatus.VERIFIED,
        "com.iqoo.website" to InstallerVerificationStatus.VERIFIED,
        "com.vivo.assistant" to InstallerVerificationStatus.VERIFIED,
        "com.vivo.easyshare" to InstallerVerificationStatus.VERIFIED,
        "com.facebook.system" to InstallerVerificationStatus.VERIFIED,
        "com.sec.android.app.samsungapps" to InstallerVerificationStatus.VERIFIED,
        "com.samsung.android.app.omcagent" to InstallerVerificationStatus.VERIFIED,
        "com.sec.android.easyMover" to InstallerVerificationStatus.VERIFIED,
        "com.sec.android.easyMover.Agent" to InstallerVerificationStatus.VERIFIED,
        "com.samsung.android.scloud" to InstallerVerificationStatus.VERIFIED,
        "com.samsung.android.themestore" to InstallerVerificationStatus.VERIFIED,
        "com.samsung.android.themecenter" to InstallerVerificationStatus.VERIFIED,
        "com.samsung.android.goodlock" to InstallerVerificationStatus.VERIFIED,
        "com.samsung.android.app.updatecenter" to InstallerVerificationStatus.VERIFIED,
        "com.samsung.android.app.watchmanager" to InstallerVerificationStatus.VERIFIED,
        "com.huawei.appmarket" to InstallerVerificationStatus.VERIFIED,
        "com.huawei.systemserver" to InstallerVerificationStatus.VERIFIED,
        "com.huawei.android.hsf" to InstallerVerificationStatus.VERIFIED,
        "com.huawei.android.hwouc" to InstallerVerificationStatus.VERIFIED,
        "com.huawei.android.thememanager" to InstallerVerificationStatus.VERIFIED,
        "com.xiaomi.market" to InstallerVerificationStatus.VERIFIED,
        "com.xiaomi.mipicks" to InstallerVerificationStatus.VERIFIED,
        "com.miui.analytics" to InstallerVerificationStatus.VERIFIED,
        "com.xiaomi.discover" to InstallerVerificationStatus.VERIFIED,
        "com.aura.oobe.deutsche" to InstallerVerificationStatus.VERIFIED,
        "com.sonyericsson.updatecenter" to InstallerVerificationStatus.VERIFIED,

        "com.apkpure.aegon" to InstallerVerificationStatus.UNVERIFIED,
        "com.qooapp.qoohelper" to InstallerVerificationStatus.UNVERIFIED,

        "com.coloros.filemanager" to InstallerVerificationStatus.SIDE_LOAD,
        "com.google.android.packageinstaller" to InstallerVerificationStatus.SIDE_LOAD,
        "com.miui.packageinstaller" to InstallerVerificationStatus.SIDE_LOAD,
        "com.android.chrome" to InstallerVerificationStatus.SIDE_LOAD,
        "com.htetznaing.zfile" to InstallerVerificationStatus.SIDE_LOAD,
    )
}

data class Installer(
    val name: String?,
    val packageName: String?,
    val verificationStatus: InstallerVerificationStatus,
)
