package com.akexorcist.ruammij.base.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [(SafeApp::class)], version = 2)
abstract class RuamMijDatabase: RoomDatabase() {
    abstract fun getSafeAppDao() : SafeAppDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Legacy decisions have no signing identity and must be confirmed again.
                db.execSQL("ALTER TABLE safe_app ADD COLUMN signingSha256 TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}
