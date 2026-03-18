package com.jsh.wirelessexplorer.core.data.di;

import com.jsh.wirelessexplorer.core.data.nfc.NfcReaderController;
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
public final class NfcDataModule_Companion_ProvideNfcReaderControllerFactory implements Factory<NfcReaderController> {
  private final Provider<NfcRepositoryImpl> implProvider;

  private NfcDataModule_Companion_ProvideNfcReaderControllerFactory(
      Provider<NfcRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public NfcReaderController get() {
    return provideNfcReaderController(implProvider.get());
  }

  public static NfcDataModule_Companion_ProvideNfcReaderControllerFactory create(
      Provider<NfcRepositoryImpl> implProvider) {
    return new NfcDataModule_Companion_ProvideNfcReaderControllerFactory(implProvider);
  }

  public static NfcReaderController provideNfcReaderController(NfcRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(NfcDataModule.Companion.provideNfcReaderController(impl));
  }
}
