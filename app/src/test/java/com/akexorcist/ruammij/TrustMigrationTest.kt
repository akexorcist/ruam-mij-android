package com.akexorcist.ruammij

import androidx.room.Room
import com.akexorcist.ruammij.base.data.database.RuamMijDatabase
import com.akexorcist.ruammij.base.data.database.SafeApp
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TrustMigrationTest {
    @Test fun upgradePreservesLegacyRowsWithoutGrantingCertificateBoundTrust() {
        val application = RuntimeEnvironment.getApplication()
        val name = "trust-migration-test"
        application.deleteDatabase(name)
        application.openOrCreateDatabase(name, 0, null).use { legacy ->
            legacy.execSQL("CREATE TABLE safe_app (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, packageName TEXT NOT NULL)")
            legacy.execSQL("INSERT INTO safe_app (packageName) VALUES ('test.app')")
            legacy.version = 1
        }
        val database = Room.databaseBuilder(application, RuamMijDatabase::class.java, name)
            .addMigrations(RuamMijDatabase.MIGRATION_1_2)
            .allowMainThreadQueries().build()
        try {
            val dao = database.getSafeAppDao()
            val legacy = dao.getAll().single()
            assertEquals("test.app", legacy.packageName)
            assertEquals("", legacy.signingSha256)
            dao.replace(SafeApp(packageName = "test.app", signingSha256 = "new-certificate"))
            assertEquals("new-certificate", dao.getAll().single().signingSha256)
        } finally {
            database.close()
            application.deleteDatabase(name)
        }
    }
}
