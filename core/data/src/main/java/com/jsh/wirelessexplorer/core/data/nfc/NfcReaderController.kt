package com.jsh.wirelessexplorer.core.data.nfc

import android.app.Activity

interface NfcReaderController {
    fun enableReader(activity: Activity)
    fun disableReader(activity: Activity)
}
