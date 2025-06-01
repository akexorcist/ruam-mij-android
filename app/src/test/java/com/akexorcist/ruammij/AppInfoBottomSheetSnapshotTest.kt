@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalRoborazziApi::class)

package com.akexorcist.ruammij

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.akexorcist.ruammij.base.data.InstalledApp
import com.akexorcist.ruammij.base.data.Installer
import com.akexorcist.ruammij.base.data.InstallerVerificationStatus
import com.akexorcist.ruammij.base.data.PermissionInfo
import com.akexorcist.ruammij.base.ui.component.AppInfoBottomSheet
import com.akexorcist.ruammij.utils.SnapshotTests
import com.akexorcist.ruammij.utils.TestRuamMijTheme
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(qualifiers = "en-w489dp-h1400dp-hdpi")
@Category(SnapshotTests::class)
class AppInfoBottomSheetSnapshotTest(
    private val installedApp: InstalledApp,
    private val useDarkMode: Boolean
) {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val dir = "appInfoBottomSheet"

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "installedApp={0}, darkMode={2}")
        fun data(): List<Array<Any>> {
            val installedTimeMs = 1722500254000L

            val installedApp = InstalledApp(
                name = "App Name",
                packageName = "com.akexorcist.ruammij",
                appVersion = "1.0.0",
                icon = null,
                systemApp = true,
                installedAt = installedTimeMs,
                installer = Installer(
                    name = "Installer Name",
                    packageName = "com.akexorcist.installer",
                    verificationStatus = InstallerVerificationStatus.VERIFIED,
                    sha256 = "12:34:56:78:90",
                ),
                sha256 = "12:34:56:78:90",
                permissions = listOf(
                    PermissionInfo(
                        name = "com.sample.permission.PERMISSION_1",
                        label = "Permission 1",
                        description = "Description 1"
                    ),
                    PermissionInfo(
                        name = "com.sample.permission.PERMISSION_2",
                        label = "Permission 2",
                        description = "Description 2"
                    ),
                ),
            )

            return listOf(
                arrayOf(installedApp, false),
                arrayOf(installedApp, true),
            )
        }
    }

    @Test
    fun appInfoBottomSheetPartial() {
        val icon = composeTestRule.activity.getDrawable(R.mipmap.ic_launcher)
        val installedApp = installedApp.copy(icon = icon)

        composeTestRule.setContent {
            TestRuamMijTheme(darkTheme = useDarkMode) {
                AppInfoBottomSheet(
                    app = installedApp,
                    onOpenInSettingClick = {},
                    onMarkAsSafeClick = {},
                    onDismissRequest = {}
                )
            }
        }

        captureScreenRoboImage("$dir/AppInfoBottomSheet.Partial.ItemCollapse${if (useDarkMode) "_darkMode" else ""}.png")

        installedApp.permissions.forEach { permission ->
            composeTestRule
                .onNodeWithText(permission.label)
                .performClick()
        }

        composeTestRule.waitForIdle()

        captureScreenRoboImage("$dir/AppInfoBottomSheet.Partial.ItemExpand${if (useDarkMode) "_darkMode" else ""}.png")
    }

    @Test
    fun appInfoBottomSheetExpanded() {
        val icon = composeTestRule.activity.getDrawable(R.mipmap.ic_launcher)
        val installedApp = installedApp.copy(icon = icon)

        composeTestRule.setContent {
            TestRuamMijTheme(darkTheme = useDarkMode) {
                AppInfoBottomSheet(
                    app = installedApp,
                    sheetState = SheetState(
                        skipPartiallyExpanded = true,
                        density = LocalDensity.current
                    ),
                    onOpenInSettingClick = {},
                    onMarkAsSafeClick = {},
                    onDismissRequest = {}
                )
            }
        }

        captureScreenRoboImage("$dir/AppInfoBottomSheet.Expand.ItemCollapse${if (useDarkMode) "_darkMode" else ""}.png")

        installedApp.permissions.forEach { permission ->
            composeTestRule
                .onNodeWithText(permission.label)
                .performClick()
        }

        composeTestRule.waitForIdle()

        captureScreenRoboImage("$dir/AppInfoBottomSheet.Expand.ItemExpand${if (useDarkMode) "_darkMode" else ""}.png")
    }

    @Test
    @Config(qualifiers = "+th")
    fun appInfoBottomSheetPartialTh() {
        val icon = composeTestRule.activity.getDrawable(R.mipmap.ic_launcher)
        val installedApp = installedApp.copy(icon = icon)

        composeTestRule.setContent {
            TestRuamMijTheme(darkTheme = useDarkMode) {
                AppInfoBottomSheet(
                    app = installedApp,
                    onOpenInSettingClick = {},
                    onMarkAsSafeClick = {},
                    onDismissRequest = {}
                )
            }
        }

        captureScreenRoboImage("$dir/AppInfoBottomSheetTh.Partial.ItemCollapse${if (useDarkMode) "_darkMode" else ""}.png")

        installedApp.permissions.forEach { permission ->
            composeTestRule
                .onNodeWithText(permission.label)
                .performClick()
        }

        composeTestRule.waitForIdle()

        captureScreenRoboImage("$dir/AppInfoBottomSheetTh.Partial.ItemExpand${if (useDarkMode) "_darkMode" else ""}.png")
    }

    @Test
    @Config(qualifiers = "+th")
    fun appInfoBottomSheetExpandTh() {
        val icon = composeTestRule.activity.getDrawable(R.mipmap.ic_launcher)
        val installedApp = installedApp.copy(icon = icon)

        composeTestRule.setContent {
            TestRuamMijTheme(darkTheme = useDarkMode) {
                AppInfoBottomSheet(
                    app = installedApp,
                    sheetState = SheetState(
                        skipPartiallyExpanded = true,
                        density = LocalDensity.current
                    ),
                    onOpenInSettingClick = {},
                    onMarkAsSafeClick = {},
                    onDismissRequest = {}
                )
            }
        }

        captureScreenRoboImage("$dir/AppInfoBottomSheetTh.Expand.ItemCollapse${if (useDarkMode) "_darkMode" else ""}.png")

        installedApp.permissions.forEach { permission ->
            composeTestRule
                .onNodeWithText(permission.label)
                .performClick()
        }

        composeTestRule.waitForIdle()

        captureScreenRoboImage("$dir/AppInfoBottomSheetTh.Expand.ItemExpand${if (useDarkMode) "_darkMode" else ""}.png")
    }
}