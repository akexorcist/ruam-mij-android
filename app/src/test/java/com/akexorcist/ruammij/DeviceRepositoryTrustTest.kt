package com.akexorcist.ruammij

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.Signature
import android.content.pm.SigningInfo
import android.os.Build
import androidx.room.Room
import com.akexorcist.ruammij.base.common.CoroutineDispatcherProvider
import com.akexorcist.ruammij.base.data.InstallerVerificationStatus
import com.akexorcist.ruammij.base.data.database.RuamMijDatabase
import com.akexorcist.ruammij.functional.device.DefaultDeviceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadow.api.Shadow

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [27, 36])
@Suppress("DEPRECATION")
class DeviceRepositoryTrustTest {
    private lateinit var application: Application
    private lateinit var database: RuamMijDatabase
    private lateinit var repository: DefaultDeviceRepository

    @Before fun setUp() {
        application = RuntimeEnvironment.getApplication()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            shadowOf(application.packageManager).setInstallSourceInfo(application.packageName, null, null)
        }
        database = Room.inMemoryDatabaseBuilder(application, RuamMijDatabase::class.java)
            .allowMainThreadQueries().build()
        repository = DefaultDeviceRepository(application, object : CoroutineDispatcherProvider {
            override fun main() = Dispatchers.Unconfined
            override fun io() = Dispatchers.IO
        }, database.getSafeAppDao())
        install("test.installer")
        install("test.app")
        install("test.sibling")
        application.packageManager.setInstallerPackageName("test.app", "test.installer")
        application.packageManager.setInstallerPackageName("test.sibling", "test.installer")
    }

    private fun install(name: String, certificate: String? = "1234") {
        shadowOf(application.packageManager).installPackage(PackageInfo().apply {
            packageName = name
            applicationInfo = ApplicationInfo().apply { packageName = name; nonLocalizedLabel = name }
            signatures = certificate?.let { arrayOf(Signature(it)) }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && certificate != null) {
                signingInfo = Shadow.newInstanceOf(SigningInfo::class.java).also {
                    shadowOf(it).setSignatures(arrayOf(Signature(certificate)))
                }
            }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val installer = if (name == "test.app" || name == "test.sibling") "test.installer" else null
            shadowOf(application.packageManager).setInstallSourceInfo(name, null, installer)
        }
    }

    @After fun tearDown() = database.close()

    @Test fun trustAndRevokeRefreshListAndDetailWithoutTrustingSiblingOrInstaller() = runBlocking {
        assertFalse(repository.getInstalledApps().first { it.packageName == "test.app" }.isTrustedByUser)
        repository.markAsSafe("test.app")
        val apps = repository.getInstalledApps()
        val trusted = apps.first { it.packageName == "test.app" }
        assertTrue(trusted.isTrustedByUser)
        assertEquals(InstallerVerificationStatus.UNVERIFIED, trusted.installer.verificationStatus)
        assertFalse(apps.first { it.packageName == "test.sibling" }.isTrustedByUser)
        assertTrue(repository.getInstalledApp(forceRefresh = true, packageName = "test.app")!!.isTrustedByUser)
        repository.markAsSafe("test.app")
        assertEquals(1, database.getSafeAppDao().getAll().count { it.packageName == "test.app" })
        repository.unmarkAsSafe("test.app")
        assertFalse(repository.getInstalledApp(packageName = "test.app")!!.isTrustedByUser)
        assertFalse(repository.getInstalledApps().first { it.packageName == "test.app" }.isTrustedByUser)
    }

    @Test fun replacingSigningCertificateRequiresNewTrustDecision() = runBlocking {
        repository.markAsSafe("test.app")
        install("test.app", "5678")
        assertFalse(repository.getInstalledApp(forceRefresh = true, packageName = "test.app")!!.isTrustedByUser)
    }

    @Test fun missingSigningIdentityCannotBeTrusted() = runBlocking {
        install("test.unsigned", certificate = null)
        try {
            repository.markAsSafe("test.unsigned")
            fail("Trust must not be stored without a certificate")
        } catch (expected: IllegalArgumentException) {
            assertTrue(database.getSafeAppDao().getAll().isEmpty())
        }
    }
}
