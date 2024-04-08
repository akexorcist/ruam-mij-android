package com.akexorcist.ruammij

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.akexorcist.ruammij.ui.RuamMijApp
import com.akexorcist.ruammij.ui.rememberAppState
import com.akexorcist.ruammij.utils.SnapshotTests
import com.akexorcist.ruammij.utils.TestRuamMijTheme
import com.github.takahirom.roborazzi.InternalRoborazziApi
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

data class TestParameterSuite(
    val screenRes: Int,
    val isDarkTheme: Boolean
)

@OptIn(InternalRoborazziApi::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(qualifiers = "en-w489dp-h1400dp-hdpi")
@Category(SnapshotTests::class)
class MainScreensSnapshotTest(
    private val screenName: String,
    private val screenRes: Int,
    private val useDarkMode: Boolean
) {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "screen={0}, darkMode={2}")
        fun data(): List<Array<Any>> {
            val screens = listOf(
                "Overview" to R.string.menu_overview,
                "Accessibility" to R.string.menu_accessibility,
                "InstalledApp" to R.string.menu_installed_app,
                "AboutApp" to R.string.menu_about_app,
            )
            return buildList {
                screens.forEach { (name, res) ->
                    add(arrayOf(name, res, true))
                    add(arrayOf(name, res, false))
                }
            }
        }
    }

    @Test
    fun mainScreens() {
        composeTestRule.setContent {
            TestRuamMijTheme(darkTheme = useDarkMode) {
                val appState = rememberAppState()
                RuamMijApp(appState = appState)
            }
        }

        val navigationText = composeTestRule.activity.getString(screenRes)
        composeTestRule
            .onNodeWithText(navigationText)
            .performClick()

        composeTestRule.onRoot()
            .captureRoboImage("MainScreens.${screenName}${if (useDarkMode) "_darkMode" else ""}.png")
    }

    @Test
    @Config(qualifiers = "+th")
    fun mainScreensTh() {
        composeTestRule.setContent {
            TestRuamMijTheme(darkTheme = useDarkMode) {
                val appState = rememberAppState()
                RuamMijApp(appState = appState)
            }
        }

        val navigationText = composeTestRule.activity.getString(screenRes)
        composeTestRule
            .onNodeWithText(navigationText)
            .performClick()

        composeTestRule.onRoot()
            .captureRoboImage("MainScreensTh.${screenName}${if (useDarkMode) "_darkMode" else ""}.png")
    }
}
