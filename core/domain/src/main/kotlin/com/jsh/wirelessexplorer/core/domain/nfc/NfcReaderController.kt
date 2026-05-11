package com.jsh.wirelessexplorer.core.domain.nfc

import android.app.Activity

interface NfcReaderController {
    fun enableReader(activity: Activity)
    fun disableReader(activity: Activity)
}
