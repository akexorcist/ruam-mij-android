package com.akexorcist.ruammij.common

object Installers {
    const val GOOGLE_PLAY = "com.android.vending"
    const val AMAZON_APPSTORE = "com.amazon.venezia"
    const val HEYTAP_APP_MARKET = "com.heytap.market"
    const val VIVO_V_APPSTORE = "com.vivo.appstore"
    const val VIVO_SYSTEM_LAUNCHER = "com.bbk.launcher2"
    const val FACEBOOK = "com.facebook.system"
    const val SAMSUNG_GALAXY_STORE = "com.sec.android.app.samsungapps"
    const val SAMSUNG_OMC_AGENT = "com.samsung.android.app.omcagent"
    const val SAMSUNG_SMART_SWITCH = "com.sec.android.easyMover"
    const val SAMSUNG_CLOUD = "com.samsung.android.scloud"
    const val HUAWEI_APP_GALLERY = "com.huawei.appmarket"
    const val HUAWEI_SYSTEM_SERVICES = "com.huawei.systemserver"
    const val HUAWEI_HMS_SERVICES_FRAMEWORK = "com.huawei.android.hsf"
    const val HUAWEI_SOFTWARE_UPDATE = "com.huawei.android.hwouc"
    const val XIAOMI_MARKET = "com.xiaomi.market"
    const val XIAOMI_GET_APPS = "com.xiaomi.mipicks"
    const val XIAOMI_ANALYTICS = "com.miui.analytics"
    const val SIDELOAD = "com.google.android.packageinstaller"
    const val COLOR_OS_MY_FILES = "com.coloros.filemanager"
    const val APK_PURE = "com.apkpure.aegon"

    fun getVerifiedInstallers() = listOf(
        GOOGLE_PLAY,
        AMAZON_APPSTORE,
        HEYTAP_APP_MARKET,
        VIVO_V_APPSTORE,
        VIVO_SYSTEM_LAUNCHER,
        FACEBOOK,
        SAMSUNG_GALAXY_STORE,
        SAMSUNG_OMC_AGENT,
        SAMSUNG_SMART_SWITCH,
        SAMSUNG_CLOUD,
        HUAWEI_APP_GALLERY,
        HUAWEI_SYSTEM_SERVICES,
        HUAWEI_HMS_SERVICES_FRAMEWORK,
        HUAWEI_SOFTWARE_UPDATE,
        XIAOMI_MARKET,
        XIAOMI_GET_APPS,
        XIAOMI_ANALYTICS,
        null,
    )
}
