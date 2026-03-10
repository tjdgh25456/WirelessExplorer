package com.jsh.wirelessexplorer.di

import android.content.Context
import android.nfc.NfcAdapter
import com.jsh.wirelessexplorer.data.datasource.NfcReader
import com.jsh.wirelessexplorer.data.repository.NfcRepositoryImpl
import com.jsh.wirelessexplorer.domain.repository.NfcRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NfcModule {

    @Provides
    @Singleton
    fun provideNfcReader(@ApplicationContext context: Context): NfcReader =
        NfcReader(NfcAdapter.getDefaultAdapter(context))

    @Provides
    @Singleton
    fun provideNfcRepository(nfcReader: NfcReader): NfcRepository =
        NfcRepositoryImpl(nfcReader)
}
