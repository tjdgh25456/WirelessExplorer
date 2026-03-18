package com.jsh.wirelessexplorer.core.database.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.jsh.wirelessexplorer.core.database.entity.DeviceHistoryEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class DeviceHistoryDao_Impl(
  __db: RoomDatabase,
) : DeviceHistoryDao {
  private val __db: RoomDatabase

  private val __deleteAdapterOfDeviceHistoryEntity: EntityDeleteOrUpdateAdapter<DeviceHistoryEntity>

  private val __upsertAdapterOfDeviceHistoryEntity: EntityUpsertAdapter<DeviceHistoryEntity>
  init {
    this.__db = __db
    this.__deleteAdapterOfDeviceHistoryEntity = object : EntityDeleteOrUpdateAdapter<DeviceHistoryEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `device_history` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: DeviceHistoryEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__upsertAdapterOfDeviceHistoryEntity = EntityUpsertAdapter<DeviceHistoryEntity>(object : EntityInsertAdapter<DeviceHistoryEntity>() {
      protected override fun createQuery(): String = "INSERT INTO `device_history` (`id`,`device_name`,`device_address`,`tech_type`,`rssi`,`is_favorite`,`connect_count`,`last_connected_at`,`extra_json`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DeviceHistoryEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.deviceName)
        statement.bindText(3, entity.deviceAddress)
        statement.bindText(4, entity.techType)
        val _tmpRssi: Int? = entity.rssi
        if (_tmpRssi == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpRssi.toLong())
        }
        val _tmp: Int = if (entity.isFavorite) 1 else 0
        statement.bindLong(6, _tmp.toLong())
        statement.bindLong(7, entity.connectCount.toLong())
        statement.bindLong(8, entity.lastConnectedAt)
        val _tmpExtraJson: String? = entity.extraJson
        if (_tmpExtraJson == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpExtraJson)
        }
      }
    }, object : EntityDeleteOrUpdateAdapter<DeviceHistoryEntity>() {
      protected override fun createQuery(): String = "UPDATE `device_history` SET `id` = ?,`device_name` = ?,`device_address` = ?,`tech_type` = ?,`rssi` = ?,`is_favorite` = ?,`connect_count` = ?,`last_connected_at` = ?,`extra_json` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: DeviceHistoryEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.deviceName)
        statement.bindText(3, entity.deviceAddress)
        statement.bindText(4, entity.techType)
        val _tmpRssi: Int? = entity.rssi
        if (_tmpRssi == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpRssi.toLong())
        }
        val _tmp: Int = if (entity.isFavorite) 1 else 0
        statement.bindLong(6, _tmp.toLong())
        statement.bindLong(7, entity.connectCount.toLong())
        statement.bindLong(8, entity.lastConnectedAt)
        val _tmpExtraJson: String? = entity.extraJson
        if (_tmpExtraJson == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpExtraJson)
        }
        statement.bindLong(10, entity.id)
      }
    })
  }

  public override suspend fun delete(entity: DeviceHistoryEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfDeviceHistoryEntity.handle(_connection, entity)
  }

  public override suspend fun upsert(entity: DeviceHistoryEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __upsertAdapterOfDeviceHistoryEntity.upsert(_connection, entity)
  }

  public override fun observeAll(): Flow<List<DeviceHistoryEntity>> {
    val _sql: String = "SELECT * FROM device_history ORDER BY last_connected_at DESC"
    return createFlow(__db, false, arrayOf("device_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDeviceName: Int = getColumnIndexOrThrow(_stmt, "device_name")
        val _columnIndexOfDeviceAddress: Int = getColumnIndexOrThrow(_stmt, "device_address")
        val _columnIndexOfTechType: Int = getColumnIndexOrThrow(_stmt, "tech_type")
        val _columnIndexOfRssi: Int = getColumnIndexOrThrow(_stmt, "rssi")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "is_favorite")
        val _columnIndexOfConnectCount: Int = getColumnIndexOrThrow(_stmt, "connect_count")
        val _columnIndexOfLastConnectedAt: Int = getColumnIndexOrThrow(_stmt, "last_connected_at")
        val _columnIndexOfExtraJson: Int = getColumnIndexOrThrow(_stmt, "extra_json")
        val _result: MutableList<DeviceHistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DeviceHistoryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDeviceName: String
          _tmpDeviceName = _stmt.getText(_columnIndexOfDeviceName)
          val _tmpDeviceAddress: String
          _tmpDeviceAddress = _stmt.getText(_columnIndexOfDeviceAddress)
          val _tmpTechType: String
          _tmpTechType = _stmt.getText(_columnIndexOfTechType)
          val _tmpRssi: Int?
          if (_stmt.isNull(_columnIndexOfRssi)) {
            _tmpRssi = null
          } else {
            _tmpRssi = _stmt.getLong(_columnIndexOfRssi).toInt()
          }
          val _tmpIsFavorite: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp != 0
          val _tmpConnectCount: Int
          _tmpConnectCount = _stmt.getLong(_columnIndexOfConnectCount).toInt()
          val _tmpLastConnectedAt: Long
          _tmpLastConnectedAt = _stmt.getLong(_columnIndexOfLastConnectedAt)
          val _tmpExtraJson: String?
          if (_stmt.isNull(_columnIndexOfExtraJson)) {
            _tmpExtraJson = null
          } else {
            _tmpExtraJson = _stmt.getText(_columnIndexOfExtraJson)
          }
          _item = DeviceHistoryEntity(_tmpId,_tmpDeviceName,_tmpDeviceAddress,_tmpTechType,_tmpRssi,_tmpIsFavorite,_tmpConnectCount,_tmpLastConnectedAt,_tmpExtraJson)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeByTechType(techType: String): Flow<List<DeviceHistoryEntity>> {
    val _sql: String = "SELECT * FROM device_history WHERE tech_type = ? ORDER BY last_connected_at DESC"
    return createFlow(__db, false, arrayOf("device_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, techType)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDeviceName: Int = getColumnIndexOrThrow(_stmt, "device_name")
        val _columnIndexOfDeviceAddress: Int = getColumnIndexOrThrow(_stmt, "device_address")
        val _columnIndexOfTechType: Int = getColumnIndexOrThrow(_stmt, "tech_type")
        val _columnIndexOfRssi: Int = getColumnIndexOrThrow(_stmt, "rssi")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "is_favorite")
        val _columnIndexOfConnectCount: Int = getColumnIndexOrThrow(_stmt, "connect_count")
        val _columnIndexOfLastConnectedAt: Int = getColumnIndexOrThrow(_stmt, "last_connected_at")
        val _columnIndexOfExtraJson: Int = getColumnIndexOrThrow(_stmt, "extra_json")
        val _result: MutableList<DeviceHistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DeviceHistoryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDeviceName: String
          _tmpDeviceName = _stmt.getText(_columnIndexOfDeviceName)
          val _tmpDeviceAddress: String
          _tmpDeviceAddress = _stmt.getText(_columnIndexOfDeviceAddress)
          val _tmpTechType: String
          _tmpTechType = _stmt.getText(_columnIndexOfTechType)
          val _tmpRssi: Int?
          if (_stmt.isNull(_columnIndexOfRssi)) {
            _tmpRssi = null
          } else {
            _tmpRssi = _stmt.getLong(_columnIndexOfRssi).toInt()
          }
          val _tmpIsFavorite: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp != 0
          val _tmpConnectCount: Int
          _tmpConnectCount = _stmt.getLong(_columnIndexOfConnectCount).toInt()
          val _tmpLastConnectedAt: Long
          _tmpLastConnectedAt = _stmt.getLong(_columnIndexOfLastConnectedAt)
          val _tmpExtraJson: String?
          if (_stmt.isNull(_columnIndexOfExtraJson)) {
            _tmpExtraJson = null
          } else {
            _tmpExtraJson = _stmt.getText(_columnIndexOfExtraJson)
          }
          _item = DeviceHistoryEntity(_tmpId,_tmpDeviceName,_tmpDeviceAddress,_tmpTechType,_tmpRssi,_tmpIsFavorite,_tmpConnectCount,_tmpLastConnectedAt,_tmpExtraJson)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun search(query: String): Flow<List<DeviceHistoryEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM device_history
        |        WHERE device_name LIKE '%' || ? || '%'
        |           OR device_address LIKE '%' || ? || '%'
        |        ORDER BY last_connected_at DESC
        |        
        """.trimMargin()
    return createFlow(__db, false, arrayOf("device_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        _argIndex = 2
        _stmt.bindText(_argIndex, query)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDeviceName: Int = getColumnIndexOrThrow(_stmt, "device_name")
        val _columnIndexOfDeviceAddress: Int = getColumnIndexOrThrow(_stmt, "device_address")
        val _columnIndexOfTechType: Int = getColumnIndexOrThrow(_stmt, "tech_type")
        val _columnIndexOfRssi: Int = getColumnIndexOrThrow(_stmt, "rssi")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "is_favorite")
        val _columnIndexOfConnectCount: Int = getColumnIndexOrThrow(_stmt, "connect_count")
        val _columnIndexOfLastConnectedAt: Int = getColumnIndexOrThrow(_stmt, "last_connected_at")
        val _columnIndexOfExtraJson: Int = getColumnIndexOrThrow(_stmt, "extra_json")
        val _result: MutableList<DeviceHistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DeviceHistoryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDeviceName: String
          _tmpDeviceName = _stmt.getText(_columnIndexOfDeviceName)
          val _tmpDeviceAddress: String
          _tmpDeviceAddress = _stmt.getText(_columnIndexOfDeviceAddress)
          val _tmpTechType: String
          _tmpTechType = _stmt.getText(_columnIndexOfTechType)
          val _tmpRssi: Int?
          if (_stmt.isNull(_columnIndexOfRssi)) {
            _tmpRssi = null
          } else {
            _tmpRssi = _stmt.getLong(_columnIndexOfRssi).toInt()
          }
          val _tmpIsFavorite: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp != 0
          val _tmpConnectCount: Int
          _tmpConnectCount = _stmt.getLong(_columnIndexOfConnectCount).toInt()
          val _tmpLastConnectedAt: Long
          _tmpLastConnectedAt = _stmt.getLong(_columnIndexOfLastConnectedAt)
          val _tmpExtraJson: String?
          if (_stmt.isNull(_columnIndexOfExtraJson)) {
            _tmpExtraJson = null
          } else {
            _tmpExtraJson = _stmt.getText(_columnIndexOfExtraJson)
          }
          _item = DeviceHistoryEntity(_tmpId,_tmpDeviceName,_tmpDeviceAddress,_tmpTechType,_tmpRssi,_tmpIsFavorite,_tmpConnectCount,_tmpLastConnectedAt,_tmpExtraJson)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: Long): DeviceHistoryEntity? {
    val _sql: String = "SELECT * FROM device_history WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDeviceName: Int = getColumnIndexOrThrow(_stmt, "device_name")
        val _columnIndexOfDeviceAddress: Int = getColumnIndexOrThrow(_stmt, "device_address")
        val _columnIndexOfTechType: Int = getColumnIndexOrThrow(_stmt, "tech_type")
        val _columnIndexOfRssi: Int = getColumnIndexOrThrow(_stmt, "rssi")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "is_favorite")
        val _columnIndexOfConnectCount: Int = getColumnIndexOrThrow(_stmt, "connect_count")
        val _columnIndexOfLastConnectedAt: Int = getColumnIndexOrThrow(_stmt, "last_connected_at")
        val _columnIndexOfExtraJson: Int = getColumnIndexOrThrow(_stmt, "extra_json")
        val _result: DeviceHistoryEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDeviceName: String
          _tmpDeviceName = _stmt.getText(_columnIndexOfDeviceName)
          val _tmpDeviceAddress: String
          _tmpDeviceAddress = _stmt.getText(_columnIndexOfDeviceAddress)
          val _tmpTechType: String
          _tmpTechType = _stmt.getText(_columnIndexOfTechType)
          val _tmpRssi: Int?
          if (_stmt.isNull(_columnIndexOfRssi)) {
            _tmpRssi = null
          } else {
            _tmpRssi = _stmt.getLong(_columnIndexOfRssi).toInt()
          }
          val _tmpIsFavorite: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp != 0
          val _tmpConnectCount: Int
          _tmpConnectCount = _stmt.getLong(_columnIndexOfConnectCount).toInt()
          val _tmpLastConnectedAt: Long
          _tmpLastConnectedAt = _stmt.getLong(_columnIndexOfLastConnectedAt)
          val _tmpExtraJson: String?
          if (_stmt.isNull(_columnIndexOfExtraJson)) {
            _tmpExtraJson = null
          } else {
            _tmpExtraJson = _stmt.getText(_columnIndexOfExtraJson)
          }
          _result = DeviceHistoryEntity(_tmpId,_tmpDeviceName,_tmpDeviceAddress,_tmpTechType,_tmpRssi,_tmpIsFavorite,_tmpConnectCount,_tmpLastConnectedAt,_tmpExtraJson)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeDeviceCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(DISTINCT device_address) FROM device_history"
    return createFlow(__db, false, arrayOf("device_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeTotalConnections(): Flow<Int?> {
    val _sql: String = "SELECT SUM(connect_count) FROM device_history"
    return createFlow(__db, false, arrayOf("device_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int?
        if (_stmt.step()) {
          val _tmp: Int?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(0).toInt()
          }
          _result = _tmp
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeBestSignal(): Flow<Int?> {
    val _sql: String = "SELECT MAX(rssi) FROM device_history"
    return createFlow(__db, false, arrayOf("device_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int?
        if (_stmt.step()) {
          val _tmp: Int?
          if (_stmt.isNull(0)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(0).toInt()
          }
          _result = _tmp
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeById(id: Long): Flow<DeviceHistoryEntity?> {
    val _sql: String = "SELECT * FROM device_history WHERE id = ?"
    return createFlow(__db, false, arrayOf("device_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDeviceName: Int = getColumnIndexOrThrow(_stmt, "device_name")
        val _columnIndexOfDeviceAddress: Int = getColumnIndexOrThrow(_stmt, "device_address")
        val _columnIndexOfTechType: Int = getColumnIndexOrThrow(_stmt, "tech_type")
        val _columnIndexOfRssi: Int = getColumnIndexOrThrow(_stmt, "rssi")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "is_favorite")
        val _columnIndexOfConnectCount: Int = getColumnIndexOrThrow(_stmt, "connect_count")
        val _columnIndexOfLastConnectedAt: Int = getColumnIndexOrThrow(_stmt, "last_connected_at")
        val _columnIndexOfExtraJson: Int = getColumnIndexOrThrow(_stmt, "extra_json")
        val _result: DeviceHistoryEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDeviceName: String
          _tmpDeviceName = _stmt.getText(_columnIndexOfDeviceName)
          val _tmpDeviceAddress: String
          _tmpDeviceAddress = _stmt.getText(_columnIndexOfDeviceAddress)
          val _tmpTechType: String
          _tmpTechType = _stmt.getText(_columnIndexOfTechType)
          val _tmpRssi: Int?
          if (_stmt.isNull(_columnIndexOfRssi)) {
            _tmpRssi = null
          } else {
            _tmpRssi = _stmt.getLong(_columnIndexOfRssi).toInt()
          }
          val _tmpIsFavorite: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp != 0
          val _tmpConnectCount: Int
          _tmpConnectCount = _stmt.getLong(_columnIndexOfConnectCount).toInt()
          val _tmpLastConnectedAt: Long
          _tmpLastConnectedAt = _stmt.getLong(_columnIndexOfLastConnectedAt)
          val _tmpExtraJson: String?
          if (_stmt.isNull(_columnIndexOfExtraJson)) {
            _tmpExtraJson = null
          } else {
            _tmpExtraJson = _stmt.getText(_columnIndexOfExtraJson)
          }
          _result = DeviceHistoryEntity(_tmpId,_tmpDeviceName,_tmpDeviceAddress,_tmpTechType,_tmpRssi,_tmpIsFavorite,_tmpConnectCount,_tmpLastConnectedAt,_tmpExtraJson)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateFavorite(id: Long, isFavorite: Boolean) {
    val _sql: String = "UPDATE device_history SET is_favorite = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Int = if (isFavorite) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun incrementConnectCount(
    address: String,
    techType: String,
    timestamp: Long,
  ) {
    val _sql: String = """
        |
        |        UPDATE device_history
        |        SET connect_count = connect_count + 1, last_connected_at = ?
        |        WHERE device_address = ? AND tech_type = ?
        |        
        """.trimMargin()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
        _argIndex = 2
        _stmt.bindText(_argIndex, address)
        _argIndex = 3
        _stmt.bindText(_argIndex, techType)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM device_history"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
