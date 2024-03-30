@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.akexorcist.ruammij.ui.installedapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.akexorcist.ruammij.R
import com.akexorcist.ruammij.common.Installer
import com.akexorcist.ruammij.common.InstallerVerificationStatus
import com.akexorcist.ruammij.data.InstalledApp
import com.akexorcist.ruammij.ui.component.AppInfoContent
import com.akexorcist.ruammij.ui.component.BodyText
import com.akexorcist.ruammij.ui.component.BoldBodyText
import com.akexorcist.ruammij.ui.component.DescriptionText
import com.akexorcist.ruammij.ui.component.HeadlineText
import com.akexorcist.ruammij.ui.component.LoadingContent
import com.akexorcist.ruammij.ui.component.OptionItemChip
import com.akexorcist.ruammij.ui.component.OutlinedButtonWithIcon
import com.akexorcist.ruammij.ui.component.TitleText
import com.akexorcist.ruammij.ui.theme.Buttons
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import com.akexorcist.ruammij.utility.DarkLightPreviews
import com.akexorcist.ruammij.utility.koinActivityViewModel
import kotlinx.coroutines.launch

@Composable
fun InstalledAppRoute(
    preferredInstaller: String?,
    preferredShowSystemApp: Boolean?,
    viewModel: InstalledAppViewModel = koinActivityViewModel(),
) {
    val uiState by viewModel.installedAppUiState.collectAsStateWithLifecycle()
    val activity = LocalContext.current as Activity

    LaunchedEffect(Unit) {
        viewModel.loadInstalledApps(
            preferredInstaller = preferredInstaller,
            preferredShowSystemApp = preferredShowSystemApp,
        )
    }

    InstalledAppScreen(
        uiState = uiState,
        onDisplayOptionApplyClick = {
            viewModel.updateDisplayOption(it)
        },
        onRecheckClick = {
            viewModel.loadInstalledApps(
                preferredInstaller = null,
                preferredShowSystemApp = null,
                forceRefresh = true,
            )
        },
        onOpenAppInSettingClick = {
            activity.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$it")
            })
        }
    )
}

@Composable
private fun InstalledAppScreen(
    uiState: InstalledAppUiState,
    onDisplayOptionApplyClick: (DisplayOption) -> Unit,
    onRecheckClick: () -> Unit,
    onOpenAppInSettingClick: (String) -> Unit
) {
    val lazyListState: LazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var showDisplayOption by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Header()

        when (uiState) {
            is InstalledAppUiState.Loading -> {
                LoadingContent(
                    title = stringResource(R.string.overview_loading_title),
                    description = stringResource(R.string.overview_loading_description),
                )
            }

            is InstalledAppUiState.InstalledAppLoaded -> {
                if (showDisplayOption) {
                    DisplayOptionBottomSheet(
                        sortBy = uiState.displayOption.sortBy,
                        showSystemApp = uiState.displayOption.showSystemApp,
                        hideVerifiedInstaller = uiState.displayOption.hideVerifiedInstaller,
                        currentSelectedInstallers = uiState.displayOption.installers,
                        installers = uiState.installers,
                        onDisplayOptionApplyClick = { option ->
                            showDisplayOption = false
                            coroutineScope.launch { lazyListState.animateScrollToItem(0) }
                            onDisplayOptionApplyClick(option)
                        },
                        onDismissRequest = { showDisplayOption = false },
                    )
                }

                InstalledAppContent(
                    lazyListState = lazyListState,
                    installedApps = uiState.displayInstalledApps,
                    isCustomDisplayOption = run {
                        val displayOption = uiState.displayOption
                        val default = DisplayOption.default
                        val isAllInstallersSelected =
                            displayOption.installers.size != uiState.installers.size
                        (displayOption.showSystemApp != default.showSystemApp ||
                                displayOption.hideVerifiedInstaller != default.hideVerifiedInstaller ||
                                displayOption.sortBy != default.sortBy) ||
                                (displayOption.installers.isNotEmpty() && isAllInstallersSelected)
                    },
                    onOpenAppInSettingClick = onOpenAppInSettingClick,
                    onRecheckClick = onRecheckClick,
                    onDisplayOptionClick = { showDisplayOption = true },
                )
            }
        }
    }
}

@Composable
private fun InstalledAppContent(
    lazyListState: LazyListState,
    installedApps: List<InstalledApp>,
    isCustomDisplayOption: Boolean,
    onOpenAppInSettingClick: (String) -> Unit,
    onRecheckClick: () -> Unit,
    onDisplayOptionClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            OutlinedButtonWithIcon(
                label = stringResource(R.string.button_recheck_overview),
                icon = rememberVectorPainter(Icons.Outlined.Refresh),
                onClick = onRecheckClick,
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButtonWithIcon(
                label = stringResource(R.string.button_display_option),
                icon = painterResource(R.drawable.ic_display_option),
                colors = when (isCustomDisplayOption) {
                    true -> ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    )

                    false -> ButtonDefaults.outlinedButtonColors()
                },
                onClick = onDisplayOptionClick,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outline),
        )
        if (installedApps.isNotEmpty()) {
            AppContent(
                lazyListState = lazyListState,
                installedApps = installedApps,
                onOpenAppInSettingClick = onOpenAppInSettingClick,
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            EmptyAppItem(text = stringResource(R.string.installed_app_empty))
        }
    }
}

@Composable
private fun Header() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        HeadlineText(text = stringResource(R.string.installed_app_title))
        Spacer(modifier = Modifier.height(4.dp))
        DescriptionText(
            text = stringResource(R.string.installed_app_description),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun AppContent(
    lazyListState: LazyListState,
    installedApps: List<InstalledApp>,
    onOpenAppInSettingClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        LazyColumn(
            state = lazyListState,
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            itemsIndexed(
                items = installedApps,
                key = { _, item -> item.packageName }) { index, installedApp ->
                AppInfoContent(
                    app = installedApp,
                    onOpenInSettingClick = { onOpenAppInSettingClick(installedApp.packageName) },
                )
                if (index != installedApps.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
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
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(6.dp),
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            painter = painterResource(R.drawable.ic_installed_app_empty),
            contentDescription = stringResource(R.string.description_empty),
            tint = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.width(8.dp))
        BodyText(text = text, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun DisplayOptionBottomSheet(
    sortBy: SortBy,
    showSystemApp: Boolean,
    hideVerifiedInstaller: Boolean,
    installers: List<Installer>?,
    currentSelectedInstallers: List<String?>,
    onDisplayOptionApplyClick: (DisplayOption) -> Unit,
    onDismissRequest: () -> Unit
) {
    var currentSortBy by remember { mutableStateOf(sortBy) }
    var currentShowSystemApp by remember { mutableStateOf(showSystemApp) }
    var currentHideVerifiedInstaller by remember { mutableStateOf(hideVerifiedInstaller) }
    var currentInstallers: List<Pair<Installer, Boolean>> by remember {
        mutableStateOf(installers
            ?.map { installer ->
                val selected = when (currentSelectedInstallers.isEmpty()) {
                    true -> true
                    false -> currentSelectedInstallers.contains(installer.packageName)
                }
                installer to selected
            }
            ?: listOf())
    }

    val activity = LocalContext.current as Activity
    val view = LocalView.current
    val darkTheme = isSystemInDarkTheme()
    DisposableEffect(Unit) {
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars =
            !darkTheme

        onDispose {
            WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars =
                darkTheme
        }
    }

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        onDismissRequest = onDismissRequest,
    ) {
        DisplayOptionContent(
            sortBy = currentSortBy,
            showSystemApp = currentShowSystemApp,
            hideVerifiedInstaller = currentHideVerifiedInstaller,
            installers = currentInstallers,
            onSortByClick = { currentSortBy = it },
            onShowSystemAppToggled = { currentShowSystemApp = it },
            onVerifiedInstallerToggled = {
                currentHideVerifiedInstaller = it
                currentInstallers = currentInstallers.map { (installer, _) -> installer to true }
            },
            onInstallerToggled = { installer, selected ->
                currentInstallers = currentInstallers.map { item ->
                    when (item.first == installer) {
                        true -> installer to selected
                        false -> item
                    }
                }
                currentHideVerifiedInstaller =
                    when (currentInstallers.any { (_, selected) -> !selected }) {
                        true -> false
                        false -> currentHideVerifiedInstaller
                    }
            },
            onDisplayOptionApplyClick = {
                onDisplayOptionApplyClick(
                    DisplayOption(
                        sortBy = currentSortBy,
                        showSystemApp = currentShowSystemApp,
                        hideVerifiedInstaller = currentHideVerifiedInstaller,
                        installers = currentInstallers
                            .filter { it.second }
                            .map { it.first.packageName },
                    )
                )
            },
            onResetDisplayOptionClick = {
                val (defaultSortBy, defaultShowSystemApp, defaultHideVerifiedInstaller, _) = DisplayOption.default
                currentSortBy = defaultSortBy
                currentShowSystemApp = defaultShowSystemApp
                currentHideVerifiedInstaller = defaultHideVerifiedInstaller
                currentInstallers = installers?.map { it to true } ?: listOf()
            },
            onCloseClick = onDismissRequest,
        )
    }
}

@Composable
private fun DisplayOptionContent(
    sortBy: SortBy,
    showSystemApp: Boolean,
    hideVerifiedInstaller: Boolean,
    installers: List<Pair<Installer, Boolean>>,
    onSortByClick: (SortBy) -> Unit,
    onShowSystemAppToggled: (Boolean) -> Unit,
    onVerifiedInstallerToggled: (Boolean) -> Unit,
    onInstallerToggled: (Installer, Boolean) -> Unit,
    onDisplayOptionApplyClick: () -> Unit,
    onResetDisplayOptionClick: () -> Unit,
    onCloseClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .verticalScroll(state = rememberScrollState()),
        ) {
            HeadlineText(text = stringResource(R.string.installed_app_display_option_title))
            Spacer(modifier = Modifier.height(4.dp))
            DescriptionText(text = stringResource(R.string.installed_app_display_option_description))
            Spacer(modifier = Modifier.height(16.dp))
            SortGroup(
                currentSortBy = sortBy,
                onSortByClick = onSortByClick,
            )
            Spacer(modifier = Modifier.height(16.dp))
            CommonFilterGroup(
                showSystemApp = showSystemApp,
                hideVerifiedInstaller = hideVerifiedInstaller,
                onShowSystemAppToggled = onShowSystemAppToggled,
                onVerifiedInstallerToggled = onVerifiedInstallerToggled,
            )
            if (installers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                InstallerFilterGroup(
                    installers = installers,
                    onInstallerToggled = onInstallerToggled,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.outline)
                .height(1.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            OutlinedButton(
                contentPadding = Buttons.ContentPadding,
                onClick = onResetDisplayOptionClick,
            ) {
                BoldBodyText(text = stringResource(R.string.installed_app_display_option_reset))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                contentPadding = Buttons.ContentPadding,
                onClick = onCloseClick,
            ) {
                BoldBodyText(text = stringResource(R.string.installed_app_display_option_close))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                contentPadding = Buttons.ContentPadding,
                onClick = onDisplayOptionApplyClick,
            ) {
                BoldBodyText(text = stringResource(R.string.installed_app_display_option_save))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SortGroup(
    currentSortBy: SortBy,
    onSortByClick: (SortBy) -> Unit,
) {
    val sorts = listOf(
        SortBy.Name to R.string.installed_app_sort_by_name,
        SortBy.PackageName to R.string.installed_app_sort_by_package_name,
        SortBy.InstalledDate to R.string.installed_app_sort_by_installed_date,
        SortBy.Installer to R.string.installed_app_sort_by_installer,
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        TitleText(text = stringResource(R.string.installed_app_display_option_sort_by))
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(modifier = Modifier.animateContentSize()) {
            sorts.forEachIndexed { index, (sortBy, labelResourceId) ->
                OptionItem(
                    selected = sortBy == currentSortBy,
                    label = stringResource(labelResourceId),
                    leadingIcon = {},
                    onClick = { onSortByClick(sortBy) },
                )
                if (index != sorts.lastIndex) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
private fun CommonFilterGroup(
    showSystemApp: Boolean,
    hideVerifiedInstaller: Boolean,
    onShowSystemAppToggled: (Boolean) -> Unit,
    onVerifiedInstallerToggled: (Boolean) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TitleText(text = stringResource(R.string.installed_app_display_option_common_display))
        Spacer(modifier = Modifier.height(4.dp))
        OptionItem(
            selected = showSystemApp,
            label = stringResource(R.string.installed_app_show_system_app),
            onClick = { onShowSystemAppToggled(!showSystemApp) }
        )
        OptionItem(
            selected = hideVerifiedInstaller,
            label = stringResource(R.string.installed_app_hide_verified_installer),
            onClick = { onVerifiedInstallerToggled(!hideVerifiedInstaller) }
        )
    }
}

@Composable
private fun OptionItem(
    selected: Boolean,
    label: String,
    leadingIcon: @Composable () -> Unit = {
        if (selected) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = rememberVectorPainter(Icons.Outlined.Check),
                contentDescription = stringResource(R.string.description_checked),
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }
    },
    onClick: () -> Unit,
) {
    OptionItemChip(
        selected = selected,
        onClick = onClick,
        label = {
            if (selected) {
                BoldBodyText(text = label)
            } else {
                BodyText(text = label)
            }
        },
        leadingIcon = leadingIcon
    )
}

@Composable
private fun InstallerFilterGroup(
    installers: List<Pair<Installer, Boolean>>,
    onInstallerToggled: (Installer, Boolean) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TitleText(text = stringResource(R.string.installed_app_display_option_installer_display))
        Spacer(modifier = Modifier.height(12.dp))
        installers.forEachIndexed { index, (installer, selected) ->
            InstallerOptionItem(
                selected = selected,
                appName = installer.name,
                packageName = installer.packageName,
                onClick = { onInstallerToggled(installer, !selected) },
            )
            if (index != installers.lastIndex) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
private fun InstallerOptionItem(
    selected: Boolean,
    appName: String?,
    packageName: String?,
    onClick: () -> Unit,
) {
    OptionItemChip(
        selected = selected,
        onClick = onClick,
        label = {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                if (appName != null) {
                    BoldBodyText(text = appName)
                }
                if (packageName != null) {
                    BodyText(text = packageName)
                }
            }
        },
        leadingIcon = {
            if (selected) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = rememberVectorPainter(Icons.Outlined.Check),
                    contentDescription = stringResource(R.string.description_checked),
                    tint = MaterialTheme.colorScheme.onSecondary,
                )
            }
        }
    )
}

private class InstalledAppUiStateProvider : PreviewParameterProvider<InstalledAppUiState> {
    val apps = listOf(
        InstalledApp(
            name = "Accessibility Service",
            packageName = "com.example.app1",
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
            packageName = "com.example.app2",
            icon = null,
            appVersion = "1.0.0.1234567890123456789012345678901234567890 (1234)",
            systemApp = true,
            installedAt = System.currentTimeMillis(),
            installer = Installer(
                name = "OS or ADB",
                packageName = null,
                verificationStatus = InstallerVerificationStatus.VERIFIED,
            ),
        ),
    )
    override val values: Sequence<InstalledAppUiState>
        get() = sequenceOf(
            InstalledAppUiState.Loading(DisplayOption.default),
            InstalledAppUiState.InstalledAppLoaded(
                displayOption = DisplayOption.default,
                installedApps = apps,
                displayInstalledApps = apps,
                installers = emptyList(),
            ),
            InstalledAppUiState.InstalledAppLoaded(
                displayOption = DisplayOption.default,
                installedApps = emptyList(),
                displayInstalledApps = emptyList(),
                installers = emptyList(),
            )
        )
}

@DarkLightPreviews
@Composable
private fun InstalledAppPreview(@PreviewParameter(InstalledAppUiStateProvider::class) uiState: InstalledAppUiState) {
    RuamMijTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
        ) {
            InstalledAppScreen(
                uiState = uiState,
                onDisplayOptionApplyClick = {},
                onRecheckClick = { },
                onOpenAppInSettingClick = {}
            )
        }
    }
}

@DarkLightPreviews
@Composable
private fun DisplayOptionContentPreview() {
    val installers = listOf(
        Installer(
            name = "OS or ADB",
            packageName = null,
            verificationStatus = InstallerVerificationStatus.VERIFIED
        ),
        Installer(
            name = "App 1",
            packageName = "com.akexorcist.appstore1",
            verificationStatus = InstallerVerificationStatus.VERIFIED
        ),
        Installer(
            name = "App 2",
            packageName = "com.akexorcist.appstore2",
            verificationStatus = InstallerVerificationStatus.VERIFIED
        ),
        Installer(
            name = null,
            packageName = "com.akexorcist.appstore3",
            verificationStatus = InstallerVerificationStatus.VERIFIED
        ),
        Installer(
            name = null,
            packageName = "com.akexorcist.appstore4",
            verificationStatus = InstallerVerificationStatus.VERIFIED
        ),
    )
    RuamMijTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
        ) {
            DisplayOptionContent(
                sortBy = SortBy.Name,
                showSystemApp = true,
                hideVerifiedInstaller = true,
                installers = installers.mapIndexed { index, installer ->
                    installer to (index % 2 == 0)
                },
                onSortByClick = {},
                onShowSystemAppToggled = {},
                onVerifiedInstallerToggled = {},
                onInstallerToggled = { _, _ -> },
                onDisplayOptionApplyClick = {},
                onResetDisplayOptionClick = {},
                onCloseClick = {},
            )
        }
    }
}
