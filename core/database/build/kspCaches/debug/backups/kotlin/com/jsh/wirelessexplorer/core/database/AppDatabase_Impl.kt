package com.jsh.wirelessexplorer.core.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.jsh.wirelessexplorer.core.database.dao.DeviceHistoryDao
import com.jsh.wirelessexplorer.core.database.dao.DeviceHistoryDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _deviceHistoryDao: Lazy<DeviceHistoryDao> = lazy {
    DeviceHistoryDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "91078286bc996c682de03138f1a48301", "a6a2529525e04edd3e1b791a56dddd9a") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `device_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `device_name` TEXT NOT NULL, `device_address` TEXT NOT NULL, `tech_type` TEXT NOT NULL, `rssi` INTEGER, `is_favorite` INTEGER NOT NULL, `connect_count` INTEGER NOT NULL, `last_connected_at` INTEGER NOT NULL, `extra_json` TEXT)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_device_history_device_address_tech_type` ON `device_history` (`device_address`, `tech_type`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '91078286bc996c682de03138f1a48301')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `device_history`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsDeviceHistory: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDeviceHistory.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDeviceHistory.put("device_name", TableInfo.Column("device_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDeviceHistory.put("device_address", TableInfo.Column("device_address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDeviceHistory.put("tech_type", TableInfo.Column("tech_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDeviceHistory.put("rssi", TableInfo.Column("rssi", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDeviceHistory.put("is_favorite", TableInfo.Column("is_favorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDeviceHistory.put("connect_count", TableInfo.Column("connect_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDeviceHistory.put("last_connected_at", TableInfo.Column("last_connected_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDeviceHistory.put("extra_json", TableInfo.Column("extra_json", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDeviceHistory: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesDeviceHistory: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesDeviceHistory.add(TableInfo.Index("index_device_history_device_address_tech_type", true, listOf("device_address", "tech_type"), listOf("ASC", "ASC")))
        val _infoDeviceHistory: TableInfo = TableInfo("device_history", _columnsDeviceHistory, _foreignKeysDeviceHistory, _indicesDeviceHistory)
        val _existingDeviceHistory: TableInfo = read(connection, "device_history")
        if (!_infoDeviceHistory.equals(_existingDeviceHistory)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |device_history(com.jsh.wirelessexplorer.core.database.entity.DeviceHistoryEntity).
              | Expected:
              |""".trimMargin() + _infoDeviceHistory + """
              |
              | Found:
              |""".trimMargin() + _existingDeviceHistory)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "device_history")
  }

  public override fun clearAllTables() {
    super.performClear(false, "device_history")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(DeviceHistoryDao::class, DeviceHistoryDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun deviceHistoryDao(): DeviceHistoryDao = _deviceHistoryDao.value
}
