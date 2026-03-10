package com.jsh.wirelessexplorer.di

import android.content.Context
import androidx.room.Room
import com.jsh.wirelessexplorer.data.local.dao.DeviceHistoryDao
import com.jsh.wirelessexplorer.data.local.database.AppDatabase
import com.jsh.wirelessexplorer.data.repository.DeviceHistoryRepositoryImpl
import com.jsh.wirelessexplorer.domain.repository.DeviceHistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideDeviceHistoryRepository(
        dao: DeviceHistoryDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): DeviceHistoryRepository = DeviceHistoryRepositoryImpl(dao, ioDispatcher)
}
