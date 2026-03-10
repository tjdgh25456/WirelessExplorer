package com.jsh.wirelessexplorer.domain.repository

import android.app.Activity
import com.jsh.wirelessexplorer.domain.model.NfcTagData
import kotlinx.coroutines.flow.Flow

interface NfcRepository {
    fun observeNfcTag(): Flow<NfcTagData>
    fun enableReader(activity: Activity)
    fun disableReader(activity: Activity)
    fun isNfcEnabled(): Boolean
    fun isNfcSupported(): Boolean
}
