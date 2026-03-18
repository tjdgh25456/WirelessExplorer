package com.jsh.wirelessexplorer.core.data.nfc;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class NfcRepositoryImpl_Factory implements Factory<NfcRepositoryImpl> {
  private final Provider<NfcReader> nfcReaderProvider;

  private NfcRepositoryImpl_Factory(Provider<NfcReader> nfcReaderProvider) {
    this.nfcReaderProvider = nfcReaderProvider;
  }

  @Override
  public NfcRepositoryImpl get() {
    return newInstance(nfcReaderProvider.get());
  }

  public static NfcRepositoryImpl_Factory create(Provider<NfcReader> nfcReaderProvider) {
    return new NfcRepositoryImpl_Factory(nfcReaderProvider);
  }

  public static NfcRepositoryImpl newInstance(NfcReader nfcReader) {
    return new NfcRepositoryImpl(nfcReader);
  }
}
