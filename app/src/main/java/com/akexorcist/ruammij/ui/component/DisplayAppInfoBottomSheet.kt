@file:OptIn(ExperimentalMaterial3Api::class)

package com.akexorcist.ruammij.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue.Expanded
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.akexorcist.ruammij.R
import com.akexorcist.ruammij.common.Installer
import com.akexorcist.ruammij.common.InstallerVerificationStatus
import com.akexorcist.ruammij.data.AdditionalInfo
import com.akexorcist.ruammij.data.InstalledApp
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import com.akexorcist.ruammij.utility.DarkLightPreviews
import com.akexorcist.ruammij.utility.toReadableDatetime
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun DisplayAppInfoBottomSheet(
    app: InstalledApp,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        onDismissRequest = onDismissRequest,
    ) {
        DisplayAppInfoContent(app = app)
    }
}

@Composable
private fun DisplayAppInfoContent(
    app: InstalledApp,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(state = rememberScrollState()),
        ) {

            HeadlineText(text = stringResource(R.string.installed_app_info_title))

            AppInfoHeaderItem(app = app)

            Spacer(modifier = Modifier.height(8.dp))

            CommonAdditionalInfoSection(app = app)

            AdditionalInfoSection(
                title = stringResource(R.string.app_info_permissions),
                items = app.permissions
            )
        }
    }
}

@Composable
private fun AppInfoHeaderItem(
    app: InstalledApp,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(64.dp),
            painter = rememberDrawablePainter(drawable = app.icon),
            contentDescription = stringResource(
                R.string.description_app_icon,
                app.name
            ),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            TitleText(text = app.name)

            BodyText(
                text = app.packageName,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (app.systemApp) {
                Spacer(modifier = Modifier.height(4.dp))
                SystemAppBadge()
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun CommonAdditionalInfoSection(
    app: InstalledApp,
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(6.dp),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            AdditionalAppInfo(
                label = stringResource(R.string.app_info_version),
                value = app.appVersion,
            )
            Spacer(modifier = Modifier.height(4.dp))
            AdditionalAppInfo(
                label = stringResource(R.string.app_info_installed_at),
                value = app.installedAt.toReadableDatetime(),
            )
            Spacer(modifier = Modifier.height(4.dp))
            AdditionalAppInfo(
                label = stringResource(R.string.app_info_installed_by),
                value = app.installer.packageName ?: "",
                verticalAlignment = Alignment.CenterVertically,
                valueContent = {
                    AppInstaller(
                        name = app.installer.name,
                        packageName = app.installer.packageName,
                        verificationStatus = app.installer.verificationStatus,
                    )
                }
            )
        }
    }
}

@Composable
private fun AdditionalInfoSection(
    title: String,
    items: List<AdditionalInfo>
) {
    if (items.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))

        TitleText(text = title)

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(6.dp),
                ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                items.forEach { permission ->
                    AdditionalInfoItem(
                        info = permission
                    )
                }
            }
        }
    }
}

@Composable
private fun AdditionalInfoItem(
    info: AdditionalInfo,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            BoldBodyText(
                text = info.label,
                color = MaterialTheme.colorScheme.onBackground
            )

            AnimatedVisibility(visible = isExpanded && info.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))

                BodyText(
                    text = info.description,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@DarkLightPreviews
@Composable
private fun DisplayAppInfoBottomSheetPreview() {
    RuamMijTheme {
        DisplayAppInfoBottomSheet(
            app = InstalledApp(
                name = "App Name",
                packageName = "com.akexorcist.ruammij",
                appVersion = "1.0.0",
                icon = ResourcesCompat.getDrawable(
                    LocalContext.current.resources,
                    R.mipmap.ic_launcher,
                    LocalContext.current.theme
                ),
                systemApp = true,
                installedAt = System.currentTimeMillis(),
                installer = Installer(
                    name = "Installer Name",
                    packageName = "com.akexorcist.installer",
                    verificationStatus = InstallerVerificationStatus.VERIFIED,
                    sha256 = "12:34:56:78:90",
                ),
                sha256 = "12:34:56:78:90",
                permissions = listOf(
                    AdditionalInfo(
                        name = "",
                        label = "Permission 1",
                        description = "Description 1"
                    ),
                    AdditionalInfo(
                        name = "",
                        label = "Permission 2",
                        description = "Description 2"
                    ),
                ),
            ),
            sheetState = SheetState(
                skipPartiallyExpanded = true,
                density = LocalDensity.current,
                initialValue = Expanded,
                skipHiddenState = true,
            ),
            onDismissRequest = { }
        )
    }
}