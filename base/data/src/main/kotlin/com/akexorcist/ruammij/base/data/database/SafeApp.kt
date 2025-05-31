package com.akexorcist.ruammij.base.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "safe_app")
data class SafeApp(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val packageName: String
)