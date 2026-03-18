package com.jsh.wirelessexplorer.core.navigation

import kotlinx.serialization.Serializable

@Serializable data object DashboardRoute
@Serializable data class DevicesRoute(val initialTab: Int = 0)
@Serializable data object AnalyzeRoute
@Serializable data object HistoryRoute

@Serializable data object SettingsRoute
@Serializable data object SearchRoute
@Serializable data object OnboardingRoute
@Serializable data class DeviceDetailRoute(val deviceId: Long)
