package com.jsh.wirelessexplorer.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.jsh.wirelessexplorer.core.data.search.RecentSearchDataSource
import com.jsh.wirelessexplorer.core.data.search.RecentSearchRepositoryImpl
import com.jsh.wirelessexplorer.core.domain.search.ClearRecentSearchesUseCase
import com.jsh.wirelessexplorer.core.domain.search.ClearRecentSearchesUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.search.DeleteRecentSearchUseCase
import com.jsh.wirelessexplorer.core.domain.search.DeleteRecentSearchUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.search.ObserveRecentSearchesUseCase
import com.jsh.wirelessexplorer.core.domain.search.ObserveRecentSearchesUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.search.RecentSearchRepository
import com.jsh.wirelessexplorer.core.domain.search.SaveRecentSearchUseCase
import com.jsh.wirelessexplorer.core.domain.search.SaveRecentSearchUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchDataModule {

    @Binds
    @Singleton
    abstract fun bindRecentSearchRepository(impl: RecentSearchRepositoryImpl): RecentSearchRepository

    @Binds
    abstract fun bindObserveRecentSearches(impl: ObserveRecentSearchesUseCaseImpl): ObserveRecentSearchesUseCase

    @Binds
    abstract fun bindSaveRecentSearch(impl: SaveRecentSearchUseCaseImpl): SaveRecentSearchUseCase

    @Binds
    abstract fun bindDeleteRecentSearch(impl: DeleteRecentSearchUseCaseImpl): DeleteRecentSearchUseCase

    @Binds
    abstract fun bindClearRecentSearches(impl: ClearRecentSearchesUseCaseImpl): ClearRecentSearchesUseCase

    companion object {

        @Provides
        @Singleton
        fun provideRecentSearchDataSource(
            dataStore: DataStore<Preferences>,
        ): RecentSearchDataSource = RecentSearchDataSource(dataStore)
    }
}
