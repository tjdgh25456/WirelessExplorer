package com.jsh.wirelessexplorer.util

import com.jsh.wirelessexplorer.core.common.TimeProvider

class FakeTimeProvider(var currentTime: Long = 0L) : TimeProvider {
    override fun now(): Long = currentTime
}
