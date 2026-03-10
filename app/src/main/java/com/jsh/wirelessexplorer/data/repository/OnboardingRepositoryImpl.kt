package com.jsh.wirelessexplorer.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.jsh.wirelessexplorer.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : OnboardingRepository {

    private val key = booleanPreferencesKey("onboarding_completed")

    override fun isOnboardingCompleted(): Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[key] ?: false }

    override suspend fun setOnboardingCompleted() {
        dataStore.edit { prefs -> prefs[key] = true }
        Timber.d("온보딩 완료 저장")
    }
}
