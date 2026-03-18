package com.jsh.wirelessexplorer.feature.uwb;

import com.jsh.wirelessexplorer.core.domain.uwb.CheckUwbSupportUseCase;
import com.jsh.wirelessexplorer.core.domain.uwb.StartUwbRangingUseCase;
import com.jsh.wirelessexplorer.core.domain.uwb.UwbRepository;
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
public final class UwbViewModel_Factory implements Factory<UwbViewModel> {
  private final Provider<CheckUwbSupportUseCase> checkUwbSupportProvider;

  private final Provider<StartUwbRangingUseCase> startUwbRangingProvider;

  private final Provider<UwbRepository> uwbRepositoryProvider;

  private UwbViewModel_Factory(Provider<CheckUwbSupportUseCase> checkUwbSupportProvider,
      Provider<StartUwbRangingUseCase> startUwbRangingProvider,
      Provider<UwbRepository> uwbRepositoryProvider) {
    this.checkUwbSupportProvider = checkUwbSupportProvider;
    this.startUwbRangingProvider = startUwbRangingProvider;
    this.uwbRepositoryProvider = uwbRepositoryProvider;
  }

  @Override
  public UwbViewModel get() {
    return newInstance(checkUwbSupportProvider.get(), startUwbRangingProvider.get(), uwbRepositoryProvider.get());
  }

  public static UwbViewModel_Factory create(
      Provider<CheckUwbSupportUseCase> checkUwbSupportProvider,
      Provider<StartUwbRangingUseCase> startUwbRangingProvider,
      Provider<UwbRepository> uwbRepositoryProvider) {
    return new UwbViewModel_Factory(checkUwbSupportProvider, startUwbRangingProvider, uwbRepositoryProvider);
  }

  public static UwbViewModel newInstance(CheckUwbSupportUseCase checkUwbSupport,
      StartUwbRangingUseCase startUwbRanging, UwbRepository uwbRepository) {
    return new UwbViewModel(checkUwbSupport, startUwbRanging, uwbRepository);
  }
}
