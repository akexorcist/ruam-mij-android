package com.akexorcist.ruammij.di

import android.app.Application
import androidx.room.Room
import com.akexorcist.ruammij.base.data.database.RuamMijDatabase
import com.akexorcist.ruammij.base.data.database.SafeAppDao
import org.koin.dsl.module

internal val databaseModule = module {
    single { provideDataBase(get()) }
    single { provideSafeAppDao(get()) }
}

private fun provideDataBase(application: Application): RuamMijDatabase =
    Room.databaseBuilder(
        application,
        RuamMijDatabase::class.java,
        "ruammij"
    ).fallbackToDestructiveMigration(false).build()

private fun provideSafeAppDao(database: RuamMijDatabase): SafeAppDao = database.getSafeAppDao()
