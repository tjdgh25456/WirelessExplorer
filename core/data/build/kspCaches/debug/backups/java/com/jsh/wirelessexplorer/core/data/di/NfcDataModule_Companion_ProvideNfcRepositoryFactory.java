package com.jsh.wirelessexplorer.core.data.di;

import com.jsh.wirelessexplorer.core.data.nfc.NfcRepositoryImpl;
import com.jsh.wirelessexplorer.core.domain.nfc.NfcRepository;
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
public final class NfcDataModule_Companion_ProvideNfcRepositoryFactory implements Factory<NfcRepository> {
  private final Provider<NfcRepositoryImpl> implProvider;

  private NfcDataModule_Companion_ProvideNfcRepositoryFactory(
      Provider<NfcRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public NfcRepository get() {
    return provideNfcRepository(implProvider.get());
  }

  public static NfcDataModule_Companion_ProvideNfcRepositoryFactory create(
      Provider<NfcRepositoryImpl> implProvider) {
    return new NfcDataModule_Companion_ProvideNfcRepositoryFactory(implProvider);
  }

  public static NfcRepository provideNfcRepository(NfcRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(NfcDataModule.Companion.provideNfcRepository(impl));
  }
}
