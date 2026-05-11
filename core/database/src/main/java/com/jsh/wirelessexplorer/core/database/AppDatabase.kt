package com.jsh.wirelessexplorer.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jsh.wirelessexplorer.core.database.dao.DeviceHistoryDao
import com.jsh.wirelessexplorer.core.database.entity.DeviceHistoryEntity

@Database(entities = [DeviceHistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviceHistoryDao(): DeviceHistoryDao
}
