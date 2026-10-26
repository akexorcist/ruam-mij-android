package com.akexorcist.ruammij

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import com.akexorcist.ruammij.feature.aboutapp.ScamSafetyGuideBottomSheet
import com.akexorcist.ruammij.functional.core.state.rememberAppState
import com.akexorcist.ruammij.ui.RuamMijApp
import com.akexorcist.ruammij.utils.TestRuamMijTheme
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@OptIn(ExperimentalRoborazziApi::class)
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "en-w360dp-h800dp-mdpi")
class ScamSafetyGuideUiTest {
    @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test fun guideActionsAndExpandableTopicsWorkInEnglish() = checkGuide("SafetyGuide.English.png")

    @Test @Config(qualifiers = "+th")
    fun guideActionsAndExpandableTopicsWorkInThai() = checkGuide("SafetyGuide.Thai.png")

    private fun checkGuide(screenshot: String) {
        var dialerRequested = false
        var openedSource: String? = null
        var dismissed = false
        composeTestRule.setContent {
            TestRuamMijTheme(darkTheme = false) {
                ScamSafetyGuideBottomSheet(
                    onDialHelp = { dialerRequested = true },
                    onOpenSource = { openedSource = it },
                    onDismissRequest = { dismissed = true },
                )
            }
        }
        val activity = composeTestRule.activity
        composeTestRule.onNodeWithText(activity.getString(R.string.safety_urgent_title)).assertIsDisplayed()
        captureScreenRoboImage(screenshot)
        composeTestRule.onNodeWithText(activity.getString(R.string.safety_dial_aoc)).performScrollTo().performClick()
        assertTrue(dialerRequested)
        val title = activity.getString(R.string.safety_impersonation_title)
        composeTestRule.onNode(hasScrollAction()).performScrollToNode(hasText(title))
        composeTestRule.onNodeWithText(title).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.safety_impersonation_body)).assertExists()
        composeTestRule.onNodeWithText(title).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.safety_impersonation_body)).assertDoesNotExist()
        val source = activity.getString(R.string.safety_source_aoc)
        composeTestRule.onNode(hasScrollAction()).performScrollToNode(hasText(source))
        composeTestRule.onNodeWithText(source).performClick()
        assertEquals("https://mdes.go.th/mission/153/", openedSource)
        composeTestRule.onNodeWithText(activity.getString(R.string.installed_app_display_option_close)).performClick()
        assertTrue(dismissed)
    }

    @Test fun aboutMenuOpensAndClosesTheOfflineGuide() {
        composeTestRule.setContent {
            TestRuamMijTheme(darkTheme = false) {
                RuamMijApp(appState = rememberAppState())
            }
        }
        val activity = composeTestRule.activity
        composeTestRule.onNodeWithText(activity.getString(R.string.menu_about_app)).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.safety_guide_title)).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.safety_urgent_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.installed_app_display_option_close)).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.safety_guide_title)).assertIsDisplayed()
        composeTestRule.onNodeWithText(activity.getString(R.string.safety_urgent_title)).assertDoesNotExist()
    }
}
