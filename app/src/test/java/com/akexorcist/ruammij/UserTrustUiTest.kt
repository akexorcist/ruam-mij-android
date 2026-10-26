package com.akexorcist.ruammij

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.akexorcist.ruammij.base.data.InstalledApp
import com.akexorcist.ruammij.base.data.Installer
import com.akexorcist.ruammij.base.data.InstallerVerificationStatus
import com.akexorcist.ruammij.base.ui.component.AppInfoBottomSheet
import com.akexorcist.ruammij.utils.TestRuamMijTheme
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@OptIn(ExperimentalMaterial3Api::class, ExperimentalRoborazziApi::class)
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "en-w360dp-h800dp-mdpi")
class UserTrustUiTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test fun trustCanBeRemovedWithoutClosingDetails() {
        val app = mutableStateOf(InstalledApp(
            name = "Example app", packageName = "test.app", appVersion = "1", icon = null,
            systemApp = false, installedAt = 0L, sha256 = "cert",
            installer = Installer("Example source", "test.source", InstallerVerificationStatus.UNVERIFIED, "store-cert"),
        ))
        composeTestRule.setContent {
            TestRuamMijTheme(darkTheme = false) {
                AppInfoBottomSheet(
                    app = app.value,
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    onOpenInSettingClick = {},
                    onMarkAsSafeClick = { app.value = app.value.copy(isTrustedByUser = !app.value.isTrustedByUser) },
                    onDismissRequest = {},
                )
            }
        }
        composeTestRule.onNodeWithText("Trust this app").performClick()
        composeTestRule.onNodeWithText("Trusted by you").assertIsDisplayed()
        composeTestRule.onNodeWithText("Remove trust").assertIsDisplayed()
        captureScreenRoboImage("UserTrust.Trusted.png")
        composeTestRule.onNodeWithText("Remove trust").performClick()
        composeTestRule.onNodeWithText("Trust this app").assertIsDisplayed()
        composeTestRule.onNodeWithText("Trusted by you").assertDoesNotExist()
    }
}
