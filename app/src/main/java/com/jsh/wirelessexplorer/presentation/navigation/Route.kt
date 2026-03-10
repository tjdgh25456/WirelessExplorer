package com.jsh.wirelessexplorer.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable data object DashboardRoute
@Serializable data object DevicesRoute
@Serializable data object AnalyzeRoute
@Serializable data object HistoryRoute

@Serializable data object SettingsRoute
@Serializable data object SearchRoute
@Serializable data object OnboardingRoute
@Serializable data class DeviceDetailRoute(val deviceId: Long)
