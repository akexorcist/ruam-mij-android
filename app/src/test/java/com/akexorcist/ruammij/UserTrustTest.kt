package com.akexorcist.ruammij

import com.akexorcist.ruammij.base.data.InstalledApp
import com.akexorcist.ruammij.base.data.Installer
import com.akexorcist.ruammij.base.data.InstallerVerificationStatus
import com.akexorcist.ruammij.base.data.database.SafeApp
import com.akexorcist.ruammij.base.data.withUserTrust
import org.junit.Assert.*
import org.junit.Test

class UserTrustTest {
    private val installer = Installer("Installer", "test.installer", InstallerVerificationStatus.SIDE_LOAD, "store-cert")
    private fun app(packageName: String = "test.app", certificate: String = "app-cert") = InstalledApp(
        name = packageName, packageName = packageName, appVersion = "1", icon = null,
        systemApp = false, installedAt = 0L, installer = installer, sha256 = certificate,
    )

    @Test fun trustRequiresBothPackageAndCertificate() {
        val decisions = listOf(SafeApp(packageName = "test.app", signingSha256 = "app-cert"))
        assertTrue(app().withUserTrust(decisions).isTrustedByUser)
        assertFalse(app(packageName = "test.other").withUserTrust(decisions).isTrustedByUser)
        assertFalse(app(certificate = "replacement-cert").withUserTrust(decisions).isTrustedByUser)
    }

    @Test fun trustingAnAppDoesNotVerifyItsInstallerOrTrustOtherApps() {
        val decisions = listOf(SafeApp(packageName = "test.app", signingSha256 = "app-cert"))
        val trusted = app().withUserTrust(decisions)
        assertEquals(InstallerVerificationStatus.SIDE_LOAD, trusted.installer.verificationStatus)
        assertFalse(app(packageName = "test.sibling").withUserTrust(decisions).isTrustedByUser)
        assertFalse(app().withUserTrust(listOf(
            SafeApp(packageName = "test.installer", signingSha256 = "store-cert"),
        )).isTrustedByUser)
    }

    @Test fun legacyOrMissingSigningIdentityCannotGrantTrust() {
        assertFalse(app().withUserTrust(listOf(SafeApp(packageName = "test.app"))).isTrustedByUser)
        assertFalse(app(certificate = "").withUserTrust(listOf(SafeApp(packageName = "test.app"))).isTrustedByUser)
    }

    @Test fun removingDecisionClearsPreviouslyComputedTrust() {
        val trusted = app().copy(isTrustedByUser = true)
        assertFalse(trusted.withUserTrust(emptyList()).isTrustedByUser)
    }
}
