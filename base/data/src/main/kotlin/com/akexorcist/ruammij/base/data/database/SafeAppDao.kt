package com.akexorcist.ruammij.base.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SafeAppDao {

    @Query("SELECT * FROM safe_app")
    fun getAll(): List<SafeApp>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insert(safeApp: SafeApp)

    @Query("DELETE FROM safe_app WHERE packageName = :packageName")
    fun delete(packageName: String)

    @Transaction
    fun replace(safeApp: SafeApp) {
        delete(safeApp.packageName)
        insert(safeApp)
    }
}
