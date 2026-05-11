package com.jsh.wirelessexplorer.core.domain.ble

interface GetBluetoothEnabledUseCase {
    operator fun invoke(): Boolean
}
