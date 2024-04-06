package com.akexorcist.ruammij

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.akexorcist.ruammij.ui.RuamMijApp
import com.akexorcist.ruammij.ui.rememberAppState
import com.akexorcist.ruammij.ui.theme.RuamMijTheme
import com.akexorcist.ruammij.utils.SnapshotTests
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "en-w489dp-h1400dp-hdpi")
@Category(SnapshotTests::class)
class MainActivityTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun overview_en() {
        composeTestRule.setContent {
            RuamMijTheme {
                val appState = rememberAppState()
                RuamMijApp(appState = appState)
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }


    @Test
    fun overview_dark_en() {
        composeTestRule.setContent {
            RuamMijTheme(darkTheme = true) {
                val appState = rememberAppState()
                RuamMijApp(appState = appState)
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    @Test
    @Config(qualifiers = "+th")
    fun overview_dark_th() {
        composeTestRule.setContent {
            RuamMijTheme(darkTheme = true) {
                val appState = rememberAppState()
                RuamMijApp(appState = appState)
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }

    @Test
    @Config(qualifiers = "+th")
    fun overview_th() {
        composeTestRule.setContent {
            RuamMijTheme {
                val appState = rememberAppState()
                RuamMijApp(appState = appState)
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage()
    }
}
