package com.jsh.wirelessexplorer.core.data.di

import android.content.Context
import android.nfc.NfcAdapter
import com.jsh.wirelessexplorer.core.common.IoDispatcher
import com.jsh.wirelessexplorer.core.data.nfc.NfcReader
import com.jsh.wirelessexplorer.core.domain.nfc.NfcReaderController
import com.jsh.wirelessexplorer.core.data.nfc.NfcRepositoryImpl
import com.jsh.wirelessexplorer.core.domain.nfc.NfcRepository
import com.jsh.wirelessexplorer.core.domain.nfc.ObserveNfcEnabledUseCase
import com.jsh.wirelessexplorer.core.domain.nfc.ObserveNfcEnabledUseCaseImpl
import com.jsh.wirelessexplorer.core.domain.nfc.ObserveNfcTagUseCase
import com.jsh.wirelessexplorer.core.domain.nfc.ObserveNfcTagUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NfcDataModule {

    @Binds
    abstract fun bindObserveNfcTag(impl: ObserveNfcTagUseCaseImpl): ObserveNfcTagUseCase

    @Binds
    abstract fun bindObserveNfcEnabled(impl: ObserveNfcEnabledUseCaseImpl): ObserveNfcEnabledUseCase

    companion object {

        @Provides
        @Singleton
        fun provideNfcReader(
            @ApplicationContext context: Context,
            @IoDispatcher ioDispatcher: CoroutineDispatcher,
        ): NfcReader = NfcReader(NfcAdapter.getDefaultAdapter(context), ioDispatcher)

        @Provides
        @Singleton
        fun provideNfcRepositoryImpl(nfcReader: NfcReader): NfcRepositoryImpl =
            NfcRepositoryImpl(nfcReader)

        @Provides
        @Singleton
        fun provideNfcRepository(impl: NfcRepositoryImpl): NfcRepository = impl

        @Provides
        @Singleton
        fun provideNfcReaderController(impl: NfcRepositoryImpl): NfcReaderController = impl
    }
}
