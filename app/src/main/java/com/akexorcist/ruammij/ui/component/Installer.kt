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
import com.akexorcist.ruammij.common.*
import com.akexorcist.ruammij.ui.theme.MaterialAdditionColorScheme

@Composable
fun AppInstaller(packageName: String?) {
    val installer = Installer.fromPackageName(packageName)
    val verificationStatus = installer?.verificationStatus ?: InstallerVerificationStatus.VERIFIED
    val name = installer?.readableName ?: packageName ?: stringResource(id = R.string.app_info_installer_unknown)
    when (verificationStatus) {
        InstallerVerificationStatus.VERIFIED -> VerifiedInstaller(name)
        InstallerVerificationStatus.UNVERIFIED -> UnverifiedInstaller(name)
        InstallerVerificationStatus.SIDE_LOAD -> SideloadInstaller(name)
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
            tint = MaterialAdditionColorScheme.colorScheme.success,
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
                color = MaterialAdditionColorScheme.colorScheme.warning,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = rememberVectorPainter(Icons.Filled.Info),
            contentDescription = stringResource(R.string.description_unsafe),
            tint = MaterialAdditionColorScheme.colorScheme.warning,
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