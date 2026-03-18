package com.jsh.wirelessexplorer.core.data.search

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

private const val MAX_RECENT_SEARCHES = 10

class RecentSearchDataSource(
    private val dataStore: DataStore<Preferences>,
) {
    private val key = stringPreferencesKey("recent_searches_json")

    /** 최근 검색어 목록을 순서 보존하여 관찰한다. */
    fun observeRecentSearches(): Flow<List<String>> =
        dataStore.data.map { prefs ->
            val raw = prefs[key] ?: return@map emptyList()
            runCatching { Json.decodeFromString<List<String>>(raw) }
                .getOrElse { emptyList() }
        }

    /** 검색어를 맨 앞에 추가한다. 중복이면 기존 위치에서 제거 후 앞으로 이동. */
    suspend fun saveSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return

        dataStore.edit { prefs ->
            val current = prefs[key]
                ?.let { runCatching { Json.decodeFromString<List<String>>(it) }.getOrNull() }
                ?: emptyList()

            val updated = buildList {
                add(trimmed)
                addAll(current.filter { it != trimmed })
            }.take(MAX_RECENT_SEARCHES)

            prefs[key] = Json.encodeToString(updated)
            Timber.d("최근 검색어 저장: $trimmed (총 ${updated.size}개)")
        }
    }

    /** 특정 검색어를 삭제한다. */
    suspend fun deleteSearch(query: String) {
        dataStore.edit { prefs ->
            val current = prefs[key]
                ?.let { runCatching { Json.decodeFromString<List<String>>(it) }.getOrNull() }
                ?: return@edit

            val updated = current.filter { it != query }
            prefs[key] = Json.encodeToString(updated)
        }
    }

    /** 최근 검색어를 모두 삭제한다. */
    suspend fun clearAll() {
        dataStore.edit { prefs ->
            prefs.remove(key)
        }
        Timber.d("최근 검색어 전체 삭제")
    }
}
