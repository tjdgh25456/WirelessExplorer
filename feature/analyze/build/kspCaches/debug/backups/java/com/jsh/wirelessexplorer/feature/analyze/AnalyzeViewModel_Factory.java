package com.jsh.wirelessexplorer.feature.analyze;

import com.jsh.wirelessexplorer.core.domain.analyze.ObserveAnalyticsSummaryUseCase;
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveTechDistributionUseCase;
import com.jsh.wirelessexplorer.core.domain.analyze.ObserveTopDevicesUseCase;
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
public final class AnalyzeViewModel_Factory implements Factory<AnalyzeViewModel> {
  private final Provider<ObserveAnalyticsSummaryUseCase> observeSummaryProvider;

  private final Provider<ObserveTechDistributionUseCase> observeDistributionProvider;

  private final Provider<ObserveTopDevicesUseCase> observeTopDevicesProvider;

  private AnalyzeViewModel_Factory(Provider<ObserveAnalyticsSummaryUseCase> observeSummaryProvider,
      Provider<ObserveTechDistributionUseCase> observeDistributionProvider,
      Provider<ObserveTopDevicesUseCase> observeTopDevicesProvider) {
    this.observeSummaryProvider = observeSummaryProvider;
    this.observeDistributionProvider = observeDistributionProvider;
    this.observeTopDevicesProvider = observeTopDevicesProvider;
  }

  @Override
  public AnalyzeViewModel get() {
    return newInstance(observeSummaryProvider.get(), observeDistributionProvider.get(), observeTopDevicesProvider.get());
  }

  public static AnalyzeViewModel_Factory create(
      Provider<ObserveAnalyticsSummaryUseCase> observeSummaryProvider,
      Provider<ObserveTechDistributionUseCase> observeDistributionProvider,
      Provider<ObserveTopDevicesUseCase> observeTopDevicesProvider) {
    return new AnalyzeViewModel_Factory(observeSummaryProvider, observeDistributionProvider, observeTopDevicesProvider);
  }

  public static AnalyzeViewModel newInstance(ObserveAnalyticsSummaryUseCase observeSummary,
      ObserveTechDistributionUseCase observeDistribution,
      ObserveTopDevicesUseCase observeTopDevices) {
    return new AnalyzeViewModel(observeSummary, observeDistribution, observeTopDevices);
  }
}
