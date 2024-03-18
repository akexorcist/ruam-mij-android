package com.akexorcist.ruammij.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.akexorcist.ruammij.R
import com.akexorcist.ruammij.common.Installers
import com.akexorcist.ruammij.ui.theme.Colors

@Composable
fun AppInstaller(packageName: String?) {
    when (packageName) {
        Installers.GOOGLE_PLAY -> VerifiedInstaller(stringResource(R.string.app_info_installer_google_play))
        Installers.APP_GALLERY -> VerifiedInstaller(stringResource(R.string.app_info_installer_huawei_app_gallery))
        Installers.AMAZON_APPSTORE -> VerifiedInstaller(stringResource(R.string.app_info_installer_amazon_appstore))
        Installers.GALAXY_STORE -> VerifiedInstaller(stringResource(R.string.app_info_installer_galaxy_store))
        Installers.APP_MARKET -> VerifiedInstaller(stringResource(R.string.app_info_installer_app_marker))
        Installers.V_APPSTORE -> VerifiedInstaller(stringResource(R.string.app_info_installer_v_appstore))
        Installers.XIAOMI_MARKET -> VerifiedInstaller(stringResource(R.string.app_info_installer_xiaomi_market))
        Installers.FACEBOOK -> VerifiedInstaller(stringResource(R.string.app_info_installer_facebook))
        Installers.SAMSUNG_OMC_AGENT -> VerifiedInstaller(stringResource(R.string.app_info_installer_samsung_omc_agent))
        Installers.SAMSUNG_SMART_SWITCH -> VerifiedInstaller(stringResource(R.string.app_info_installer_samsung_smart_switch))
        Installers.SAMSUNG_CLOUD -> VerifiedInstaller(stringResource(R.string.app_info_installer_samsung_cloud))
        Installers.VIVO_SYSTEM_LAUNCHER -> VerifiedInstaller(stringResource(R.string.app_info_installer_vivo_system_launcher))
        Installers.HUAWEI_SYSTEM_SERVICES -> VerifiedInstaller(stringResource(R.string.app_info_installer_huawei_system_services))
        Installers.HUAWEI_HMS_SERVICES_FRAMEWORK -> VerifiedInstaller(stringResource(R.string.app_info_installer_huawei_hmd_services_framework))
        Installers.HUAWEI_SOFTWARE_UPDATE -> VerifiedInstaller(stringResource(R.string.app_info_installer_huawei_software_update))
        Installers.SIDELOAD -> SideloadInstaller(stringResource(R.string.app_info_installer_sideload))
        Installers.COLOR_OS_MY_FILES -> UnverifiedInstaller(stringResource(R.string.app_info_installer_color_os_my_files))
        Installers.APK_PURE -> UnverifiedInstaller(stringResource(R.string.app_info_installer_apk_pure))
        null -> VerifiedInstaller(stringResource(R.string.app_info_installer_unknown))
        else -> UnverifiedInstaller(packageName)
    }
}

@Composable
private fun VerifiedInstaller(
    name: String,
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = rememberVectorPainter(Icons.Filled.CheckCircle),
            contentDescription = stringResource(R.string.description_safe),
            tint = Colors.Green500,
        )
        Spacer(modifier = Modifier.width(4.dp))
        LabelText(text = name)
    }
}

@Composable
private fun UnverifiedInstaller(
    name: String,
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Colors.Amber700,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = rememberVectorPainter(Icons.Filled.Info),
            contentDescription = stringResource(R.string.description_unsafe),
            tint = Colors.Amber700,
        )
        Spacer(modifier = Modifier.width(4.dp))
        LabelText(text = name)
    }
}

@Composable
private fun SideloadInstaller(
    name: String,
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.error,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = rememberVectorPainter(Icons.Filled.Warning),
            contentDescription = "Danger",
            tint = MaterialTheme.colorScheme.error,
        )
        Spacer(modifier = Modifier.width(4.dp))
        LabelText(text = name)
    }
}