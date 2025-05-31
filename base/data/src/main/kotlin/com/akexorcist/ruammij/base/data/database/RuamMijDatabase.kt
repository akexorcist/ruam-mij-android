package com.akexorcist.ruammij.base.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akexorcist.ruammij.base.data.database.SafeApp
import com.akexorcist.ruammij.base.data.database.SafeAppDao

@Database(entities = [(SafeApp::class)], version = 1)
abstract class RuamMijDatabase: RoomDatabase() {
    abstract fun getSafeAppDao() : SafeAppDao
}