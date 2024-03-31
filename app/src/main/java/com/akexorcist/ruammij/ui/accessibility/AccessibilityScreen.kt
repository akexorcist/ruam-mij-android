package com.akexorcist.ruammij.ui.accessibility

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akexorcist.ruammij.R
import com.akexorcist.ruammij.common.Installer
import com.akexorcist.ruammij.common.InstallerVerificationStatus
import com.akexorcist.ruammij.data.InstalledApp
import com.akexorcist.ruammij.ui.component.AppInfoContent
import com.akexorcist.ruammij.ui.component.BodyText
import com.akexorcist.ruammij.ui.component.DescriptionText
import com.akexorcist.ruammij.ui.component.HeadlineText
import com.akexorcist.ruammij.ui.component.LoadingContent
import com.akexorcist.ruammij.ui.component.OutlinedButtonWithIcon
import com.akexorcist.ruammij.ui.component.SectionCard
import com.akexorcist.ruammij.ui.component.TitleText
import com.akexorcist.ruammij.ui.theme.MaterialAdditionColorScheme
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccessibilityRoute(
    viewModel: AccessibilityViewModel = koinViewModel(),
) {
    val activity = LocalContext.current as Activity
    val uiState by viewModel.accessibilityUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.loadAccessibilityApps() }

    AccessibilityScreen(
        uiState = uiState,
        onAppOpenInSettingClick = { packageName ->
            activity.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
            })
        },
        onMarkAsSafeClick = {},
        onRecheckClick = { viewModel.loadAccessibilityApps(forceRefresh = true) },
    )
}

@Composable
private fun AccessibilityScreen(
    uiState: AccessibilityUiState,
    onAppOpenInSettingClick: (String) -> Unit,
    onMarkAsSafeClick: (String) -> Unit,
    onRecheckClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(state = rememberScrollState()),
    ) {
        Header()
        when (uiState) {
            is AccessibilityUiState.Loading -> {
                LoadingAccessibilityContent()
            }

            is AccessibilityUiState.AccessibilityAppLoaded -> {
                AccessibilityContent(
                    activeAccessibilityApps = uiState.active,
                    inactiveAccessibilityApps = uiState.inactive,
                    onAppOpenInSettingClick = onAppOpenInSettingClick,
                    onMarkAsSafeClick = onMarkAsSafeClick,
                    onRecheckClick = onRecheckClick,
                )
            }
        }
    }
}

@Composable
private fun LoadingAccessibilityContent() {
    Column {
        Spacer(modifier = Modifier.height(128.dp))
        LoadingContent(
            title = stringResource(R.string.accessibility_loading_title),
            description = stringResource(R.string.accessibility_loading_description),
        )
    }
}

@Composable
private fun AccessibilityContent(
    activeAccessibilityApps: List<InstalledApp>,
    inactiveAccessibilityApps: List<InstalledApp>,
    onRecheckClick: () -> Unit,
    onAppOpenInSettingClick: (String) -> Unit,
    onMarkAsSafeClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedButtonWithIcon(
            label = stringResource(R.string.button_recheck_overview),
            icon = rememberVectorPainter(Icons.Outlined.Refresh),
            onClick = onRecheckClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        RunningAccessibilityServiceApps(
            apps = activeAccessibilityApps,
            onAppOpenInSettingClick = onAppOpenInSettingClick,
            onMarkAsSafeClick = onMarkAsSafeClick,

        )
        Spacer(modifier = Modifier.height(16.dp))
        InactiveAccessibilityServiceApps(
            apps = inactiveAccessibilityApps,
            onAppOpenInSettingClick = onAppOpenInSettingClick,
            onMarkAsSafeClick = onMarkAsSafeClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun Header() {
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        HeadlineText(text = stringResource(R.string.accessibility_title))
        Spacer(modifier = Modifier.height(4.dp))
        DescriptionText(text = stringResource(R.string.accessibility_description))
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun RunningAccessibilityServiceApps(
    apps: List<InstalledApp>,
    onAppOpenInSettingClick: (String) -> Unit,
    onMarkAsSafeClick: (String) -> Unit,
) {
    SectionCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            TitleText(text = stringResource(R.string.accessibility_active_apps_title))
            Spacer(modifier = Modifier.height(4.dp))
            DescriptionText(text = stringResource(R.string.accessibility_active_apps_description_1))
            Spacer(modifier = Modifier.height(4.dp))
            DescriptionText(text = stringResource(R.string.accessibility_active_apps_description_2))
            Spacer(modifier = Modifier.height(16.dp))
            if (apps.isNotEmpty()) {
                apps.forEachIndexed { index, app ->
                    AppInfoContent(
                        app = app,
                        onOpenInSettingClick = { onAppOpenInSettingClick(app.packageName) },
                        onMarkAsSafeClick = { onMarkAsSafeClick(app.packageName) }
                    )
                    if (index != apps.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                EmptyAppItem(
                    text = stringResource(R.string.accessibility_empty_active_app),
                )
            }
        }
    }
}

@Composable
private fun InactiveAccessibilityServiceApps(
    apps: List<InstalledApp>,
    onAppOpenInSettingClick: (String) -> Unit,
    onMarkAsSafeClick: (String) -> Unit,
) {
    SectionCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            TitleText(text = stringResource(R.string.accessibility_inactive_apps_title))
            Spacer(modifier = Modifier.height(4.dp))
            DescriptionText(text = stringResource(R.string.accessibility_inactive_apps_description))
            Spacer(modifier = Modifier.height(4.dp))
            Spacer(modifier = Modifier.height(16.dp))
            if (apps.isNotEmpty()) {
                apps.forEachIndexed { index, app ->
                    AppInfoContent(
                        app = app,
                        onOpenInSettingClick = { onAppOpenInSettingClick(app.packageName) },
                        onMarkAsSafeClick = { onMarkAsSafeClick(app.packageName) }
                    )
                    if (index != apps.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                EmptyAppItem(
                    text = stringResource(R.string.accessibility_empty_inactive_app),
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

@Preview
@Composable
private fun AccessibilityContentPreview() {
    RuamMijTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            AccessibilityContent(
                activeAccessibilityApps = emptyList(),
                inactiveAccessibilityApps = emptyList(),
                onAppOpenInSettingClick = {},
                onMarkAsSafeClick = {},
                onRecheckClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun RunningAccessibilityServiceAppsPreview() {
    RuamMijTheme {
        RunningAccessibilityServiceApps(
            apps = listOf(
                InstalledApp(
                    name = "Accessibility Service",
                    packageName = "com.example.accessibility",
                    icon = null,
                    appVersion = "1.0.0 (1234)",
                    systemApp = false,
                    installedAt = System.currentTimeMillis(),
                    installer = Installer(
                        name = "Google Play",
                        packageName = "com.android.vending",
                        verificationStatus = InstallerVerificationStatus.VERIFIED,
                    ),
                ),
                InstalledApp(
                    name = "Accessibility Service",
                    packageName = "com.example.accessibility",
                    icon = null,
                    appVersion = "1.0.0.1234567890123456789012345678901234567890 (1234)",
                    systemApp = true,
                    installedAt = System.currentTimeMillis(),
                    installer = Installer(
                        name = "OS and ADB",
                        packageName = null,
                        verificationStatus = InstallerVerificationStatus.VERIFIED,
                    ),
                ),
            ),
            onAppOpenInSettingClick = {},
            onMarkAsSafeClick = {}
        )
    }
}

@Preview
@Composable
private fun EmptyRunningAccessibilityServiceAppsPreview() {
    RuamMijTheme {
        RunningAccessibilityServiceApps(
            apps = emptyList(),
            onAppOpenInSettingClick = {},
            onMarkAsSafeClick = {},
        )
    }
}

@Preview
@Composable
private fun InactiveAccessibilityServiceAppsPreview() {
    RuamMijTheme {
        InactiveAccessibilityServiceApps(
            apps = listOf(
                InstalledApp(
                    name = "Accessibility Service",
                    packageName = "com.example.accessibility",
                    icon = null,
                    appVersion = "1.0.0 (1234)",
                    systemApp = false,
                    installedAt = System.currentTimeMillis(),
                    installer = Installer(
                        name = "Google Play",
                        packageName = "com.android.vending",
                        verificationStatus = InstallerVerificationStatus.VERIFIED,
                    ),
                ),
                InstalledApp(
                    name = "Accessibility Service",
                    packageName = "com.example.accessibility",
                    icon = null,
                    appVersion = "1.0.0.1234567890123456789012345678901234567890 (1234)",
                    systemApp = true,
                    installedAt = System.currentTimeMillis(),
                    installer = Installer(
                        name = "OS and ADB",
                        packageName = null,
                        verificationStatus = InstallerVerificationStatus.VERIFIED,
                    ),
                ),
            ),
            onAppOpenInSettingClick = {},
            onMarkAsSafeClick = {},
        )
    }
}

@Preview
@Composable
private fun EmptyInactiveAccessibilityServiceAppsPreview() {
    RuamMijTheme {
        InactiveAccessibilityServiceApps(
            apps = emptyList(),
            onAppOpenInSettingClick = {},
            onMarkAsSafeClick = {},
        )
    }
}
