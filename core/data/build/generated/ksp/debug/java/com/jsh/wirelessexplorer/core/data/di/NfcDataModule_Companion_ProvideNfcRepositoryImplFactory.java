package com.jsh.wirelessexplorer.core.data.di;

import com.jsh.wirelessexplorer.core.data.nfc.NfcReader;
import com.jsh.wirelessexplorer.core.data.nfc.NfcRepositoryImpl;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class NfcDataModule_Companion_ProvideNfcRepositoryImplFactory implements Factory<NfcRepositoryImpl> {
  private final Provider<NfcReader> nfcReaderProvider;

  private NfcDataModule_Companion_ProvideNfcRepositoryImplFactory(
      Provider<NfcReader> nfcReaderProvider) {
    this.nfcReaderProvider = nfcReaderProvider;
  }

  @Override
  public NfcRepositoryImpl get() {
    return provideNfcRepositoryImpl(nfcReaderProvider.get());
  }

  public static NfcDataModule_Companion_ProvideNfcRepositoryImplFactory create(
      Provider<NfcReader> nfcReaderProvider) {
    return new NfcDataModule_Companion_ProvideNfcRepositoryImplFactory(nfcReaderProvider);
  }

  public static NfcRepositoryImpl provideNfcRepositoryImpl(NfcReader nfcReader) {
    return Preconditions.checkNotNullFromProvides(NfcDataModule.Companion.provideNfcRepositoryImpl(nfcReader));
  }
}
