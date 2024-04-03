package com.akexorcist.ruammij

import com.akexorcist.ruammij.di.AppModule.allModules
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.AutoCloseKoinTest
import org.koin.test.verify.verifyAll
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(KoinExperimentalAPI::class)
class ModuleCheckTest : AutoCloseKoinTest() {

    @Test
    fun checkKoinModules() {
        allModules.verifyAll()
    }
}
