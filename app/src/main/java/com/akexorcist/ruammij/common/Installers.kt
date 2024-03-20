package com.akexorcist.ruammij.common


enum class InstallerVerificationStatus {
    VERIFIED,
    UNVERIFIED,
    SIDE_LOAD,
}

enum class Installer(
    val packageName: String?,
    val readableName: String?,
    val verificationStatus: InstallerVerificationStatus,
) {
    GOOGLE_PLAY(
        "com.android.vending",
        "Google Play",
        InstallerVerificationStatus.VERIFIED,
    ),
    AMAZON_APPSTORE(
        "com.amazon.venezia",
        "Amazon Appstore",
        InstallerVerificationStatus.VERIFIED,
    ),
    HEYTAP_APP_MARKET(
        "com.heytap.market",
        "App Market",
        InstallerVerificationStatus.VERIFIED,
    ),
    VIVO_V_APPSTORE(
        "com.vivo.appstore",
        "vivo V-Appstore",
        InstallerVerificationStatus.VERIFIED,
    ),
    VIVO_SYSTEM_LAUNCHER(
        "com.bbk.launcher2",
        "vivo System Launcher",
        InstallerVerificationStatus.VERIFIED,
    ),
    FACEBOOK(
        "com.facebook.system",
        "Facebook",
        InstallerVerificationStatus.VERIFIED,
    ),
    SAMSUNG_GALAXY_STORE(
        "com.sec.android.app.samsungapps",
        "Samsung Galaxy Store",
        InstallerVerificationStatus.VERIFIED,
    ),
    SAMSUNG_OMC_AGENT(
        "com.samsung.android.app.omcagent",
        "Samsung OMC Agent",
        InstallerVerificationStatus.VERIFIED,
    ),
    SAMSUNG_SMART_SWITCH(
        "com.sec.android.easyMover",
        "Samsung Smart Switch",
        InstallerVerificationStatus.VERIFIED,
    ),
    SAMSUNG_CLOUD(
        "com.samsung.android.scloud",
        "Samsung Cloud",
        InstallerVerificationStatus.VERIFIED,
    ),
    HUAWEI_APP_GALLERY(
        "com.huawei.appmarket",
        "HUAWEI AppGallery",
        InstallerVerificationStatus.VERIFIED,
    ),
    HUAWEI_SYSTEM_SERVICES(
        "com.huawei.systemserver",
        "HUAWEI System Services",
        InstallerVerificationStatus.VERIFIED,
    ),
    HUAWEI_HMS_SERVICES_FRAMEWORK(
        "com.huawei.android.hsf",
        "HUAWEI HMS Services Framework",
        InstallerVerificationStatus.VERIFIED
    ),
    HUAWEI_SOFTWARE_UPDATE(
        "com.huawei.android.hwouc",
        "HUAWEI Software Update",
        InstallerVerificationStatus.VERIFIED,
    ),
    XIAOMI_MARKET(
        "com.xiaomi.market",
        "Xiaomi Market",
        InstallerVerificationStatus.VERIFIED,
    ),
    XIAOMI_GET_APPS(
        "com.xiaomi.mipicks",
        "Xiaomi GetApps",
        InstallerVerificationStatus.VERIFIED,
    ),
    XIAOMI_ANALYTICS(
        "com.miui.analytics",
        "Xiaomi Analytics",
        InstallerVerificationStatus.VERIFIED,
    ),
    COLOR_OS_MY_FILES(
        "com.coloros.filemanager",
        "ColorOS My Files",
        InstallerVerificationStatus.UNVERIFIED,
    ),
    APK_PURE(
        "com.apkpure.aegon",
        "APKPure",
        InstallerVerificationStatus.UNVERIFIED,
    ),
    SIDE_LOAD(
        "com.google.android.packageinstaller",
        "Sideload",
        InstallerVerificationStatus.SIDE_LOAD,
    ),
    PRE_INSTALLED(
        null,
        null,
        InstallerVerificationStatus.VERIFIED,
    );

    companion object {
        fun fromPackageName(packageName: String?) = entries.firstOrNull { it.packageName == packageName }
    }
}
