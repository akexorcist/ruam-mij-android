package com.akexorcist.ruammij.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SafeAppDao {

    @Query("SELECT * FROM safe_app")
    fun getAll(): List<SafeApp>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(safeApp: SafeApp)
}