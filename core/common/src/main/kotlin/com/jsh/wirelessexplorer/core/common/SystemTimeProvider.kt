package com.jsh.wirelessexplorer.core.common

import javax.inject.Inject

class SystemTimeProvider @Inject constructor() : TimeProvider {
    override fun now(): Long = System.currentTimeMillis()
}
