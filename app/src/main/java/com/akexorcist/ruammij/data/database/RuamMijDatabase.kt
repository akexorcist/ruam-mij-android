package com.akexorcist.ruammij.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(SafeApp::class)], version = 1)
abstract class RuamMijDatabase: RoomDatabase() {
    abstract fun getSafeAppDao() : SafeAppDao
}