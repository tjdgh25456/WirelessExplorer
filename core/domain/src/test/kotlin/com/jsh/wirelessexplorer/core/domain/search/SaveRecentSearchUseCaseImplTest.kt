package com.jsh.wirelessexplorer.core.domain.search

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveRecentSearchUseCaseImplTest {

    @Test
    fun invoke_delegatesSaveToRepository() = runTest {
        val repository = FakeRecentSearchRepository()
        val useCase = SaveRecentSearchUseCaseImpl(repository)

        useCase("bluetooth speaker")

        assertEquals(listOf("bluetooth speaker"), repository.savedQueries)
    }
}

private class FakeRecentSearchRepository : RecentSearchRepository {
    val savedQueries = mutableListOf<String>()

    override fun observeRecentSearches(): Flow<List<String>> = flowOf(savedQueries.toList())
    override suspend fun saveSearch(query: String) { savedQueries.add(query) }
    override suspend fun deleteSearch(query: String) { savedQueries.remove(query) }
    override suspend fun clearAll() { savedQueries.clear() }
}
