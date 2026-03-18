package com.jsh.wirelessexplorer.feature.nfc;

import com.jsh.wirelessexplorer.core.data.nfc.NfcReaderController;
import com.jsh.wirelessexplorer.core.domain.nfc.NfcRepository;
import com.jsh.wirelessexplorer.core.domain.nfc.ObserveNfcTagUseCase;
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
public final class NfcViewModel_Factory implements Factory<NfcViewModel> {
  private final Provider<ObserveNfcTagUseCase> observeNfcTagProvider;

  private final Provider<NfcRepository> nfcRepositoryProvider;

  private final Provider<NfcReaderController> nfcReaderControllerProvider;

  private NfcViewModel_Factory(Provider<ObserveNfcTagUseCase> observeNfcTagProvider,
      Provider<NfcRepository> nfcRepositoryProvider,
      Provider<NfcReaderController> nfcReaderControllerProvider) {
    this.observeNfcTagProvider = observeNfcTagProvider;
    this.nfcRepositoryProvider = nfcRepositoryProvider;
    this.nfcReaderControllerProvider = nfcReaderControllerProvider;
  }

  @Override
  public NfcViewModel get() {
    return newInstance(observeNfcTagProvider.get(), nfcRepositoryProvider.get(), nfcReaderControllerProvider.get());
  }

  public static NfcViewModel_Factory create(Provider<ObserveNfcTagUseCase> observeNfcTagProvider,
      Provider<NfcRepository> nfcRepositoryProvider,
      Provider<NfcReaderController> nfcReaderControllerProvider) {
    return new NfcViewModel_Factory(observeNfcTagProvider, nfcRepositoryProvider, nfcReaderControllerProvider);
  }

  public static NfcViewModel newInstance(ObserveNfcTagUseCase observeNfcTag,
      NfcRepository nfcRepository, NfcReaderController nfcReaderController) {
    return new NfcViewModel(observeNfcTag, nfcRepository, nfcReaderController);
  }
}
