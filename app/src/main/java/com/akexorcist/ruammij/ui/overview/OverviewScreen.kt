package com.akexorcist.ruammij.ui.overview

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.akexorcist.ruammij.R
import com.akexorcist.ruammij.SharedEventViewModel
import com.akexorcist.ruammij.data.InstalledApp
import com.akexorcist.ruammij.ui.accessibility.navigateToAccessibility
import com.akexorcist.ruammij.ui.component.AppInstaller
import com.akexorcist.ruammij.ui.component.BodyText
import com.akexorcist.ruammij.ui.component.BoldBodyText
import com.akexorcist.ruammij.ui.component.BoldLabelText
import com.akexorcist.ruammij.ui.component.DescriptionText
import com.akexorcist.ruammij.ui.component.HeadlineText
import com.akexorcist.ruammij.ui.component.LabelText
import com.akexorcist.ruammij.ui.component.LoadingContent
import com.akexorcist.ruammij.ui.component.OutlinedButtonWithIcon
import com.akexorcist.ruammij.ui.component.SectionCard
import com.akexorcist.ruammij.ui.component.TitleText
import com.akexorcist.ruammij.ui.installedapp.navigateToInstalledApp
import com.akexorcist.ruammij.ui.theme.Buttons
import com.akexorcist.ruammij.ui.theme.MaterialAdditionColorScheme
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import com.akexorcist.ruammij.utility.koinActivityViewModel
import com.akexorcist.ruammij.utility.toReadableDatetime
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import org.koin.androidx.compose.koinViewModel

@Composable
fun OverviewRoute(
    navController: NavController,
    viewModel: OverviewViewModel = koinViewModel(),
    sharedEventViewModel: SharedEventViewModel = koinActivityViewModel(),
) {
    val activity = LocalContext.current as Activity
    val uiState by viewModel.overviewUiState.collectAsStateWithLifecycle()
    val mediaProjectionDetectionEvent by sharedEventViewModel.mediaProjectionEvent.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(Unit) { viewModel.checkDevicePrivacy() }

    LaunchedEffect(mediaProjectionDetectionEvent) {
        val event = mediaProjectionDetectionEvent ?: return@LaunchedEffect
        viewModel.updateMediaProjectionEventStatus(event = event)
    }

    OverviewScreen(
        uiState = uiState,
        onRecheckClick = { viewModel.checkDevicePrivacy() },
        onOpenDrawOverOtherAppsClick = {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            activity.startActivity(intent)
        },
        onAccessibilityAppClick = { navController.navigateToAccessibility() },
        onUnverifiedInstallerClick = { installer ->
            navController.navigateToInstalledApp(
                installer = installer,
                showSystemApp = true,
            )
        },
    )
}

@Composable
private fun OverviewScreen(
    uiState: OverviewUiState,
    onRecheckClick: () -> Unit,
    onOpenDrawOverOtherAppsClick: () -> Unit,
    onAccessibilityAppClick: (String) -> Unit,
    onUnverifiedInstallerClick: (String?) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(state = rememberScrollState()),
    ) {
        Header()
        when (uiState) {
            is OverviewUiState.Loading -> {
                LoadingOverviewContent()
            }

            is OverviewUiState.Complete -> {
                OverviewContent(
                    uiState = uiState,
                    onRecheckClick = onRecheckClick,
                    onOpenDrawOverOtherAppsClick = onOpenDrawOverOtherAppsClick,
                    onAccessibilityAppClick = onAccessibilityAppClick,
                    onUnverifiedInstallerClick = onUnverifiedInstallerClick,
                )
            }
        }
    }
}

@Composable
private fun Header() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(24.dp))
        HeadlineText(text = stringResource(R.string.overview_title))
        Spacer(modifier = Modifier.height(4.dp))
        DescriptionText(text = stringResource(R.string.overview_subtitle))
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun LoadingOverviewContent() {
    Column {
        Spacer(modifier = Modifier.height(128.dp))
        LoadingContent(
            title = stringResource(R.string.overview_loading_title),
            description = stringResource(R.string.overview_loading_description),
        )
    }
}

@Composable
private fun OverviewContent(
    uiState: OverviewUiState.Complete,
    onRecheckClick: () -> Unit,
    onOpenDrawOverOtherAppsClick: () -> Unit,
    onAccessibilityAppClick: (String) -> Unit,
    onUnverifiedInstallerClick: (String?) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedButtonWithIcon(
            label = stringResource(R.string.button_recheck_overview),
            icon = rememberVectorPainter(Icons.Outlined.Refresh),
            onClick = onRecheckClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        DeviceConfigSection(
            usbDebugging = uiState.usbDebugging,
            wirelessDebugging = uiState.wirelessDebugging,
            developerOptions = uiState.developerOptions,
        )
        Spacer(modifier = Modifier.height(16.dp))
        UnverifiedInstalledAppSection(
            installedApps = uiState.unknownInstaller,
            onUnverifiedInstallerClick = onUnverifiedInstallerClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        MediaProjectionSection(
            runningApps = uiState.mediaProjectionApps,
        )
        Spacer(modifier = Modifier.height(16.dp))
        RunningAccessibilitySection(
            runningApps = uiState.runningAccessibilityApps,
            onAppClick = onAccessibilityAppClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppearOnTopSection(
            onOpenDrawOverOtherAppsClick = onOpenDrawOverOtherAppsClick,
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DeviceConfigSection(
    usbDebugging: Boolean,
    wirelessDebugging: Boolean?,
    developerOptions: Boolean,
) {
    SectionCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Title(
                text = stringResource(R.string.overview_device_configuration_title),
                icon = painterResource(R.drawable.ic_section_developer_menu),
            )
            Spacer(modifier = Modifier.height(4.dp))
            DescriptionText(text = stringResource(R.string.overview_device_configuration_description))
            Spacer(modifier = Modifier.height(24.dp))
            DeviceConfigItem(
                label = stringResource(R.string.overview_usb_debugging_label),
                additionalLabel = null,
                enabled = usbDebugging,
            )
            Spacer(modifier = Modifier.height(12.dp))
            DeviceConfigItem(
                label = stringResource(R.string.overview_wireless_debugging_label),
                additionalLabel = stringResource(R.string.overview_wireless_debugging_additional_label),
                enabled = wirelessDebugging,
            )
            Spacer(modifier = Modifier.height(12.dp))
            DeviceConfigItem(
                label = stringResource(R.string.overview_developer_options_label),
                additionalLabel = null,
                enabled = developerOptions,
            )
        }
    }
}

@Composable
private fun DeviceConfigItem(
    label: String,
    additionalLabel: String?,
    enabled: Boolean?,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            BoldBodyText(text = label)
            if (additionalLabel != null) {
                LabelText(text = additionalLabel)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Badge(
            containerColor = when {
                enabled == true -> MaterialAdditionColorScheme.colorScheme.successContainer
                else -> MaterialTheme.colorScheme.tertiaryContainer
            }
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                BoldLabelText(
                    text = when (enabled) {
                        true -> stringResource(R.string.state_enabled)
                        false -> stringResource(R.string.state_disabled)
                        null -> stringResource(R.string.state_unsupported)
                    }
                )
            }
        }
    }
}

@Composable
private fun UnverifiedInstalledAppSection(
    installedApps: List<InstalledApp>,
    onUnverifiedInstallerClick: (String?) -> Unit,
) {
    SectionCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Title(
                text = stringResource(R.string.overview_unverified_installed_app_title),
                icon = painterResource(R.drawable.ic_section_unverified_installer),
            )
            Spacer(modifier = Modifier.height(4.dp))
            DescriptionText(text = stringResource(R.string.overview_unverified_installed_app_description_1))
            Spacer(modifier = Modifier.height(4.dp))
            DescriptionText(text = stringResource(R.string.overview_unverified_installed_app_description_2))
            Spacer(modifier = Modifier.height(16.dp))
            UnverifiedInstalledAppGroup(
                installedApps = installedApps,
                onInstallerClick = onUnverifiedInstallerClick,
            )
        }
    }
}

@Composable
private fun UnverifiedInstalledAppGroup(
    installedApps: List<InstalledApp>,
    onInstallerClick: (String?) -> Unit,
) {
    val groups = installedApps.groupBy { it.installer }
    if (groups.isNotEmpty()) {
        groups.onEachIndexed { index, (installer, app) ->
            UnverifiedInstalledAppItem(
                installer = installer,
                appCount = app.size,
                onInstallerClick = onInstallerClick,
            )
            if (index != groups.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    } else {
        EmptyAppItem(
            text = stringResource(R.string.overview_unverified_installed_app_empty),
        )
    }
}

@Composable
private fun UnverifiedInstalledAppItem(
    installer: String?,
    appCount: Int,
    onInstallerClick: (String?) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(6.dp),
            )
            .clip(shape = RoundedCornerShape(6.dp))
            .clickable { onInstallerClick(installer) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = rememberVectorPainter(Icons.Outlined.Warning),
            contentDescription = stringResource(R.string.description_unsafe),
            tint = MaterialAdditionColorScheme.colorScheme.warning,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            BoldBodyText(text = installer ?: stringResource(R.string.installed_app_installer_unknown))
            BodyText(text = pluralStringResource(R.plurals.installed_app_installer_amount, appCount, appCount))
        }
        Icon(
            modifier = Modifier.size(24.dp),
            painter = rememberVectorPainter(Icons.AutoMirrored.Filled.KeyboardArrowRight),
            contentDescription = stringResource(R.string.description_more_info),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun MediaProjectionSection(
    runningApps: List<MediaProjectionApp>,
) {
    SectionCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Title(
                text = stringResource(R.string.overview_media_projection_title),
                icon = painterResource(R.drawable.ic_section_media_projection),
            )
            Spacer(modifier = Modifier.height(4.dp))
            DescriptionText(text = stringResource(R.string.overview_media_projection_description))
            Spacer(modifier = Modifier.height(16.dp))
            if (runningApps.isNotEmpty()) {
                runningApps.forEachIndexed { index, (app, state, _, updatedAt) ->
                    MediaProjectionAppItem(
                        app = app,
                        state = state,
                        updatedAt = updatedAt,
                    )
                    if (index != runningApps.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                EmptyAppItem(
                    text = stringResource(R.string.overview_media_projection_empty),
                )
            }
        }
    }
}

@Composable
private fun EmptyAppItem(
    text: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(6.dp),
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = MaterialAdditionColorScheme.colorScheme.successContainer,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = rememberVectorPainter(Icons.Outlined.Check),
                contentDescription = stringResource(R.string.description_safe),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        BodyText(text = text)
    }
}

@Composable
private fun MediaProjectionAppItem(
    app: InstalledApp,
    state: MediaProjectionState,
    updatedAt: Long,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(6.dp),
            )
            .padding(8.dp),
    ) {
        Box(modifier = Modifier.padding(top = 4.dp)) {
            Image(
                modifier = Modifier.size(32.dp),
                painter = rememberDrawablePainter(drawable = app.icon),
                contentDescription = stringResource(R.string.description_app_icon, app.name),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            BoldBodyText(
                text = app.name,
                color = MaterialTheme.colorScheme.primary,
            )
            BodyText(text = app.packageName)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BoldLabelText(text = stringResource(R.string.app_info_installed_by))
                Spacer(modifier = Modifier.width(4.dp))
                AppInstaller(app.installer)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                LabelText(
                    text = stringResource(
                        when (state) {
                            MediaProjectionState.AUTO_DETECTED,
                            MediaProjectionState.MANUAL_DETECTED -> R.string.overview_media_projection_running

                            MediaProjectionState.DEACTIVATED -> R.string.overview_media_projection_deactivated
                        }
                    ),
                    color = when (state) {
                        MediaProjectionState.AUTO_DETECTED,
                        MediaProjectionState.MANUAL_DETECTED -> MaterialAdditionColorScheme.colorScheme.success

                        MediaProjectionState.DEACTIVATED -> MaterialAdditionColorScheme.colorScheme.warning
                    }
                )
                Spacer(modifier = Modifier.width(2.dp))
                LabelText(
                    text = stringResource(
                        R.string.overview_media_projection_auto_detected_when,
                        updatedAt.toReadableDatetime()
                    )
                )
            }
        }
    }
}

@Composable
private fun RunningAccessibilitySection(
    runningApps: List<InstalledApp>,
    onAppClick: (String) -> Unit,
) {
    SectionCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Title(
                text = stringResource(R.string.overview_running_accessibility_service_title),
                icon = painterResource(R.drawable.ic_section_accessibility),
            )
            Spacer(modifier = Modifier.height(4.dp))
            DescriptionText(text = stringResource(R.string.overview_running_accessibility_service_description))
            Spacer(modifier = Modifier.height(16.dp))
            if (runningApps.isNotEmpty()) {
                runningApps.forEachIndexed { index, app ->
                    AccessibilityAppItem(
                        app = app,
                        onClick = { onAppClick(app.packageName) },
                    )
                    if (index != runningApps.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                EmptyAppItem(
                    text = stringResource(R.string.overview_running_accessibility_service_empty_app),
                )
            }
        }
    }
}

@Composable
private fun AccessibilityAppItem(
    app: InstalledApp,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(6.dp),
            )
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = rememberDrawablePainter(drawable = app.icon),
            contentDescription = stringResource(R.string.description_app_icon, app.name),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            BoldBodyText(text = app.name)
            BodyText(text = app.packageName)
        }
        Icon(
            modifier = Modifier.size(24.dp),
            painter = rememberVectorPainter(Icons.AutoMirrored.Filled.KeyboardArrowRight),
            contentDescription = stringResource(R.string.description_more_info),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}


@Composable
private fun AppearOnTopSection(
    onOpenDrawOverOtherAppsClick: () -> Unit
) {
    SectionCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Title(
                text = stringResource(R.string.overview_draw_over_other_app_title),
                icon = painterResource(R.drawable.ic_section_draw_over),
            )
            Spacer(modifier = Modifier.height(4.dp))
            DescriptionText(text = stringResource(R.string.overview_draw_over_other_app_description))
            Spacer(modifier = Modifier.height(8.dp))
            DescriptionText(text = stringResource(R.string.overview_draw_over_other_app_button_description))
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                contentPadding = Buttons.ContentPadding,
                onClick = onOpenDrawOverOtherAppsClick,
            ) {
                Text(text = stringResource(R.string.button_open_draw_over_other_app))
            }
        }
    }
}

@Composable
private fun Title(
    text: String,
    icon: Painter,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.width(8.dp))
        TitleText(text = text)
    }
}

@Preview
@Composable
private fun CheckedContentPreview() {
    RuamMijTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            OverviewContent(
                uiState = OverviewUiState.Complete(
                    usbDebugging = true,
                    wirelessDebugging = false,
                    developerOptions = true,
                    mediaProjectionApps = listOf(
                        MediaProjectionApp(
                            app = InstalledApp(
                                name = "Privacy Checker",
                                packageName = "com.akexorcist.app1",
                                appVersion = "",
                                icon = null,
                                systemApp = false,
                                installedAt = System.currentTimeMillis(),
                                installer = "com.akexorcist.appstore",
                            ),
                            state = MediaProjectionState.AUTO_DETECTED,
                            displayId = 1,
                            updatedAt = System.currentTimeMillis(),
                        ),
                        MediaProjectionApp(
                            app = InstalledApp(
                                "Privacy Checker", "com.akexorcist.app2",
                                appVersion = "",
                                icon = null,
                                systemApp = false,
                                installedAt = System.currentTimeMillis(),
                                installer = "com.akexorcist.appstore",
                            ),
                            state = MediaProjectionState.DEACTIVATED,
                            displayId = 2,
                            updatedAt = System.currentTimeMillis(),
                        ),
                    ),
                    runningAccessibilityApps = listOf(
                        InstalledApp(
                            name = "Privacy Checker",
                            packageName = "com.akexorcist.app1",
                            appVersion = "",
                            icon = null,
                            systemApp = false,
                            installedAt = System.currentTimeMillis(),
                            installer = "com.akexorcist.appstore",
                        ),
                    ),
                    unknownInstaller = listOf(
                        InstalledApp(
                            name = "Privacy Checker",
                            packageName = "com.akexorcist.app1",
                            appVersion = "",
                            icon = null,
                            systemApp = false,
                            installedAt = System.currentTimeMillis(),
                            installer = "com.akexorcist.appstore",
                        ),
                    ),
                ),
                onRecheckClick = {},
                onOpenDrawOverOtherAppsClick = {},
                onAccessibilityAppClick = {},
                onUnverifiedInstallerClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun DeviceConfigSectionPreview() {
    RuamMijTheme {
        DeviceConfigSection(
            usbDebugging = true,
            wirelessDebugging = false,
            developerOptions = true,
        )
    }
}

@Preview
@Composable
private fun UnverifiedInstalledAppSectionPreview() {
    RuamMijTheme {
        UnverifiedInstalledAppSection(
            installedApps = listOf(
                InstalledApp(
                    name = "App 1",
                    packageName = "com.akexorcist.app1",
                    appVersion = "",
                    icon = null,
                    systemApp = false,
                    installedAt = System.currentTimeMillis(),
                    installer = "com.akexorcist.appstore",
                ),
                InstalledApp(
                    name = "App 2",
                    packageName = "com.akexorcist.app2",
                    appVersion = "",
                    icon = null,
                    systemApp = false,
                    installedAt = System.currentTimeMillis(),
                    installer = "com.akexorcist.appstore",
                ),
                InstalledApp(
                    name = "App 3",
                    packageName = "com.akexorcist.app3",
                    appVersion = "",
                    icon = null,
                    systemApp = false,
                    installedAt = System.currentTimeMillis(),
                    installer = "com.external.appstore",
                ),
            ),
            onUnverifiedInstallerClick = {},
        )
    }
}

@Preview
@Composable
private fun EmptyUnverifiedInstalledAppSectionPreview() {
    RuamMijTheme {
        UnverifiedInstalledAppSection(
            installedApps = listOf(),
            onUnverifiedInstallerClick = {},
        )
    }
}

@Preview
@Composable
private fun MediaProjectionSectionPreview() {
    RuamMijTheme {
        MediaProjectionSection(
            runningApps = listOf(
                MediaProjectionApp(
                    app = InstalledApp(
                        name = "Privacy Checker",
                        packageName = "com.akexorcist.app1",
                        appVersion = "",
                        icon = null,
                        systemApp = false,
                        installedAt = System.currentTimeMillis(),
                        installer = "",
                    ),
                    state = MediaProjectionState.MANUAL_DETECTED,
                    displayId = 1,
                    updatedAt = System.currentTimeMillis(),
                ),
                MediaProjectionApp(
                    app = InstalledApp(
                        "Privacy Checker", "com.akexorcist.app2",
                        appVersion = "",
                        icon = null,
                        systemApp = false,
                        installedAt = System.currentTimeMillis(),
                        installer = "",
                    ),
                    state = MediaProjectionState.AUTO_DETECTED,
                    displayId = 2,
                    updatedAt = System.currentTimeMillis(),
                ),
            )
        )
    }
}

@Preview
@Composable
private fun EmptyMediaProjectionSectionPreview() {
    RuamMijTheme {
        MediaProjectionSection(
            runningApps = listOf()
        )
    }
}

@Preview
@Composable
private fun RunningAccessibilitySectionPreview() {
    RuamMijTheme {
        RunningAccessibilitySection(
            runningApps = listOf(
                InstalledApp(
                    name = "Privacy Checker",
                    packageName = "com.akexorcist.app1",
                    appVersion = "",
                    icon = null,
                    systemApp = false,
                    installedAt = System.currentTimeMillis(),
                    installer = "",
                ),
                InstalledApp(
                    "Privacy Checker", "com.akexorcist.app2",
                    appVersion = "",
                    icon = null,
                    systemApp = false,
                    installedAt = System.currentTimeMillis(),
                    installer = "",
                ),
            ),
            onAppClick = {},
        )
    }
}

@Preview
@Composable
private fun EmptyRunningAccessibilitySectionPreview() {
    RuamMijTheme {
        RunningAccessibilitySection(
            runningApps = listOf(),
            onAppClick = {},
        )
    }
}

@Preview
@Composable
private fun AppearOnTopSectionPreview() {
    RuamMijTheme {
        AppearOnTopSection(
            onOpenDrawOverOtherAppsClick = {}
        )
    }
}