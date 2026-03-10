package com.jsh.wirelessexplorer.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.jsh.wirelessexplorer.data.datasource.RecentSearchDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {

    @Provides
    @Singleton
    fun provideRecentSearchDataSource(
        dataStore: DataStore<Preferences>,
    ): RecentSearchDataSource = RecentSearchDataSource(dataStore)
}
