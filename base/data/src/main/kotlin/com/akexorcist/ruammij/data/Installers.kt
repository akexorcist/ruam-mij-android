package com.akexorcist.ruammij.data

sealed class ReferenceInstallerStatus {
    data class Verified(val sha256: String?) : ReferenceInstallerStatus()
    data object Unverified : ReferenceInstallerStatus()
    data object SideLoad : ReferenceInstallerStatus()
}

enum class InstallerVerificationStatus {
    VERIFIED,
    UNVERIFIED,
    SIDE_LOAD,
}

object Installers {
    val apps: Map<String?, ReferenceInstallerStatus> = mapOf(
        null to ReferenceInstallerStatus.Verified(
            sha256 = null,
        ),
        "android" to ReferenceInstallerStatus.Verified(
            sha256 = null,
        ),
        "com.android.vending" to ReferenceInstallerStatus.Verified(
            sha256 = null,
        ),
        "com.amazon.venezia" to ReferenceInstallerStatus.Verified(
            sha256 = "2F:19:AD:EB:28:4E:B3:6F:7F:07:78:61:52:B9:A1:D1:4B:21:65:32:03:AD:0B:04:EB:BF:9C:73:AB:6D:76:25",
        ),
        "com.heytap.market" to ReferenceInstallerStatus.Verified(
            sha256 = "28:9C:C3:42:9F:49:64:60:B6:6A:C2:A8:76:C0:40:09:1A:AE:F5:5B:5E:B9:42:C2:CE:69:7E:F3:B1:20:14:59",
        ),
        "com.vivo.appstore" to ReferenceInstallerStatus.Verified(
            sha256 = "BC:C3:5D:4D:36:06:F1:54:F0:40:2A:B7:63:4E:84:90:C0:B2:44:C2:67:5C:3C:62:38:98:69:87:02:4F:0C:02",
        ),
        "com.bbk.appstore" to ReferenceInstallerStatus.Verified(
            sha256 = "BC:C3:5D:4D:36:06:F1:54:F0:40:2A:B7:63:4E:84:90:C0:B2:44:C2:67:5C:3C:62:38:98:69:87:02:4F:0C:02",
        ),
        "com.bbk.launcher2" to ReferenceInstallerStatus.Verified(
            sha256 = "BC:C3:5D:4D:36:06:F1:54:F0:40:2A:B7:63:4E:84:90:C0:B2:44:C2:67:5C:3C:62:38:98:69:87:02:4F:0C:02",
        ),
        "com.bbk.theme" to ReferenceInstallerStatus.Verified(
            sha256 = "BC:C3:5D:4D:36:06:F1:54:F0:40:2A:B7:63:4E:84:90:C0:B2:44:C2:67:5C:3C:62:38:98:69:87:02:4F:0C:02",
        ),
        "com.iqoo.website" to ReferenceInstallerStatus.Verified(
            sha256 = "BC:C3:5D:4D:36:06:F1:54:F0:40:2A:B7:63:4E:84:90:C0:B2:44:C2:67:5C:3C:62:38:98:69:87:02:4F:0C:02",
        ),
        "com.vivo.assistant" to ReferenceInstallerStatus.Verified(
            sha256 = "BC:C3:5D:4D:36:06:F1:54:F0:40:2A:B7:63:4E:84:90:C0:B2:44:C2:67:5C:3C:62:38:98:69:87:02:4F:0C:02",
        ),
        "com.vivo.easyshare" to ReferenceInstallerStatus.Verified(
            sha256 = "BC:C3:5D:4D:36:06:F1:54:F0:40:2A:B7:63:4E:84:90:C0:B2:44:C2:67:5C:3C:62:38:98:69:87:02:4F:0C:02",
        ),
        "com.facebook.system" to ReferenceInstallerStatus.Verified(
            sha256 = "B9:21:09:B5:90:95:95:5C:CA:AF:5E:CB:CF:0F:15:3E:C9:64:0A:46:58:C1:01:66:7A:D9:6B:7E:49:9A:40:92",
        ),
        "com.sec.android.app.samsungapps" to ReferenceInstallerStatus.Verified(
            sha256 = "FB:A3:AF:4E:77:57:D9:01:6E:95:3F:B3:EE:46:71:CA:2B:D9:AF:72:5F:9A:53:D5:2E:D4:A3:8E:AA:A0:89:01",
        ),
        "com.samsung.android.app.omcagent" to ReferenceInstallerStatus.Verified(
            sha256 = "34:DF:0E:7A:9F:1C:F1:89:2E:45:C0:56:B4:97:3C:D8:1C:CF:14:8A:40:50:D1:1A:EA:4A:C5:A6:5F:90:0A:42",
        ),
        "com.sec.android.easyMover" to ReferenceInstallerStatus.Verified(
            sha256 = "34:DF:0E:7A:9F:1C:F1:89:2E:45:C0:56:B4:97:3C:D8:1C:CF:14:8A:40:50:D1:1A:EA:4A:C5:A6:5F:90:0A:42",
        ),
        "com.sec.android.easyMover.Agent" to ReferenceInstallerStatus.Verified(
            sha256 = "34:DF:0E:7A:9F:1C:F1:89:2E:45:C0:56:B4:97:3C:D8:1C:CF:14:8A:40:50:D1:1A:EA:4A:C5:A6:5F:90:0A:42",
        ),
        "com.samsung.android.scloud" to ReferenceInstallerStatus.Verified(
            sha256 = "34:DF:0E:7A:9F:1C:F1:89:2E:45:C0:56:B4:97:3C:D8:1C:CF:14:8A:40:50:D1:1A:EA:4A:C5:A6:5F:90:0A:42",
        ),
        "com.samsung.android.themestore" to ReferenceInstallerStatus.Verified(
            sha256 = "FB:A3:AF:4E:77:57:D9:01:6E:95:3F:B3:EE:46:71:CA:2B:D9:AF:72:5F:9A:53:D5:2E:D4:A3:8E:AA:A0:89:01",
        ),
        "com.samsung.android.themecenter" to ReferenceInstallerStatus.Verified(
            sha256 = "34:DF:0E:7A:9F:1C:F1:89:2E:45:C0:56:B4:97:3C:D8:1C:CF:14:8A:40:50:D1:1A:EA:4A:C5:A6:5F:90:0A:42",
        ),
        "com.samsung.android.goodlock" to ReferenceInstallerStatus.Verified(
            sha256 = "34:DF:0E:7A:9F:1C:F1:89:2E:45:C0:56:B4:97:3C:D8:1C:CF:14:8A:40:50:D1:1A:EA:4A:C5:A6:5F:90:0A:42",
        ),
        "com.samsung.android.app.updatecenter" to ReferenceInstallerStatus.Verified(
            sha256 = "34:DF:0E:7A:9F:1C:F1:89:2E:45:C0:56:B4:97:3C:D8:1C:CF:14:8A:40:50:D1:1A:EA:4A:C5:A6:5F:90:0A:42",
        ),
        "com.samsung.android.app.watchmanager" to ReferenceInstallerStatus.Verified(
            sha256 = "34:DF:0E:7A:9F:1C:F1:89:2E:45:C0:56:B4:97:3C:D8:1C:CF:14:8A:40:50:D1:1A:EA:4A:C5:A6:5F:90:0A:42",
        ),
        "com.huawei.appmarket" to ReferenceInstallerStatus.Verified(
            sha256 = "3B:AF:59:A2:E5:33:1C:30:67:5F:AB:35:FF:5F:FF:0D:11:61:42:D3:D4:66:4F:1C:3C:B8:04:06:8B:40:61:4F",
        ),
        "com.huawei.systemserver" to ReferenceInstallerStatus.Verified(
            sha256 = "60:3A:C6:A5:7E:20:23:E0:0C:9C:93:BB:53:9C:A6:53:DF:30:03:EB:A4:E9:2E:A1:90:4B:A4:AA:A5:D9:38:F0",
        ),
        "com.huawei.android.hsf" to ReferenceInstallerStatus.Verified(
            sha256 = "6C:C4:66:63:C9:CF:CA:F3:01:94:F6:1A:6E:18:8A:4C:4A:9D:6B:5B:15:BA:0C:F4:43:B1:97:08:AF:ED:30:40",
        ),
        "com.huawei.android.hwouc" to ReferenceInstallerStatus.Verified(
            sha256 = "60:3A:C6:A5:7E:20:23:E0:0C:9C:93:BB:53:9C:A6:53:DF:30:03:EB:A4:E9:2E:A1:90:4B:A4:AA:A5:D9:38:F0",
        ),
        "com.huawei.android.thememanager" to ReferenceInstallerStatus.Verified(
            sha256 = "B9:F0:05:35:F2:C1:B6:B9:FF:F3:1F:98:8D:00:47:44:BD:69:D8:6B:35:DD:B0:55:2D:AD:0E:C2:2A:04:17:02",
        ),
        "com.xiaomi.market" to ReferenceInstallerStatus.Verified(
            sha256 = "C9:00:9D:01:EB:F9:F5:D0:30:2B:C7:1B:2F:E9:AA:9A:47:A4:32:BB:A1:73:08:A3:11:1B:75:D7:B2:14:90:25",
        ),
        "com.xiaomi.mipicks" to ReferenceInstallerStatus.Verified(
            sha256 = "C9:00:9D:01:EB:F9:F5:D0:30:2B:C7:1B:2F:E9:AA:9A:47:A4:32:BB:A1:73:08:A3:11:1B:75:D7:B2:14:90:25",
        ),
        "com.miui.analytics" to ReferenceInstallerStatus.Verified(
            sha256 = "C9:00:9D:01:EB:F9:F5:D0:30:2B:C7:1B:2F:E9:AA:9A:47:A4:32:BB:A1:73:08:A3:11:1B:75:D7:B2:14:90:25",
        ),
        "com.xiaomi.discover" to ReferenceInstallerStatus.Verified(
            sha256 = "C9:00:9D:01:EB:F9:F5:D0:30:2B:C7:1B:2F:E9:AA:9A:47:A4:32:BB:A1:73:08:A3:11:1B:75:D7:B2:14:90:25",
        ),
        "com.aura.oobe.deutsche" to ReferenceInstallerStatus.Verified(
            sha256 = "E5:46:31:05:5C:AE:46:F5:E9:8E:C6:EA:71:06:55:C5:A1:C7:0E:38:F4:2F:E5:54:98:67:AD:8D:25:C9:46:19",
        ),
        "com.sonyericsson.updatecenter" to ReferenceInstallerStatus.Verified(
            sha256 = "63:39:37:5A:C2:95:CB:0C:D2:28:11:B9:7A:CC:D4:01:04:BD:4A:01:85:D4:DD:22:89:B8:18:60:C1:5D:62:3C",
        ),
        "com.asus.dm" to ReferenceInstallerStatus.Verified(
            sha256 = "FE:C2:3C:C8:22:96:8E:99:0D:7C:3B:4B:88:1A:C0:E8:6B:88:7F:32:0F:DB:38:E4:F4:15:65:94:CB:9C:1A:2F",
        ),

        "com.apkpure.aegon" to ReferenceInstallerStatus.Unverified,
        "com.qooapp.qoohelper" to ReferenceInstallerStatus.Unverified,

        "com.coloros.filemanager" to ReferenceInstallerStatus.SideLoad,
        "com.google.android.packageinstaller" to ReferenceInstallerStatus.SideLoad,
        "com.miui.packageinstaller" to ReferenceInstallerStatus.SideLoad,
        "com.android.chrome" to ReferenceInstallerStatus.SideLoad,
        "com.htetznaing.zfile" to ReferenceInstallerStatus.SideLoad,
    )
}

data class Installer(
    val name: String?,
    val packageName: String?,
    val verificationStatus: InstallerVerificationStatus,
    val sha256: String,
)