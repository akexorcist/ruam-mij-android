package com.akexorcist.ruammij.common

object Installers {
    const val GOOGLE_PLAY = "com.android.vending"
    const val APP_GALLERY = "com.huawei.appmarket"
    const val AMAZON_APPSTORE = "com.amazon.venezia"
    const val GALAXY_STORE = "com.sec.android.app.samsungapps"
    const val APP_MARKET = "com.heytap.market"
    const val V_APPSTORE = "com.vivo.appstore"
    const val XIAOMI_MARKET = "com.xiaomi.market"
    const val FACEBOOK = "com.facebook.system"
    const val SAMSUNG_OMC_AGENT = "com.samsung.android.app.omcagent"
    const val SAMSUNG_SMART_SWITCH = "com.sec.android.easyMover"
    const val SAMSUNG_CLOUD = "com.samsung.android.scloud"
    const val VIVO_SYSTEM_LAUNCHER = "com.bbk.launcher2"
    const val HUAWEI_SYSTEM_SERVICES = "com.huawei.systemserver"
    const val HUAWEI_HMS_SERVICES_FRAMEWORK = "com.huawei.android.hsf"
    const val HUAWEI_SOFTWARE_UPDATE = "com.huawei.android.hwouc"
    const val SIDELOAD = "com.google.android.packageinstaller"
    const val COLOR_OS_MY_FILES = "com.coloros.filemanager"
    const val APK_PURE = "com.apkpure.aegon"

    fun getVerifiedInstallers() = listOf(
        GOOGLE_PLAY,
        APP_GALLERY,
        AMAZON_APPSTORE,
        GALAXY_STORE,
        APP_MARKET,
        V_APPSTORE,
        XIAOMI_MARKET,
        FACEBOOK,
        SAMSUNG_OMC_AGENT,
        SAMSUNG_SMART_SWITCH,
        SAMSUNG_CLOUD,
        VIVO_SYSTEM_LAUNCHER,
        HUAWEI_SYSTEM_SERVICES,
        HUAWEI_HMS_SERVICES_FRAMEWORK,
        HUAWEI_SOFTWARE_UPDATE,
        null,
    )
}
