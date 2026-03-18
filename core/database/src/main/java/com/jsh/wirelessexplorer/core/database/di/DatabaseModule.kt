package com.jsh.wirelessexplorer.core.database.di

import android.content.Context
import androidx.room.Room
import com.jsh.wirelessexplorer.core.database.AppDatabase
import com.jsh.wirelessexplorer.core.database.dao.DeviceHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "wireless_explorer.db")
            .build()

    @Provides
    fun provideDeviceHistoryDao(database: AppDatabase): DeviceHistoryDao =
        database.deviceHistoryDao()
}
