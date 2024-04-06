package com.akexorcist.ruammij

import android.R
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.akexorcist.ruammij.utils.ScreenshotTests
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "en-xlarge-long-hdpi")
@Category(ScreenshotTests::class)
class MainActivityTest : KoinTest {

    @Test
    fun overview_en() {
        val activityScenario = launch(MainActivity::class.java)

        onView(withId(R.id.content))
            .captureRoboImage()
    }

    @Test
    @Config(qualifiers = "+th")
    fun overview_th() {
        val activityScenario = launch(MainActivity::class.java)

        onView(withId(R.id.content))
            .captureRoboImage()
    }
}
