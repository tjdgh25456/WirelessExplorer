package com.jsh.wirelessexplorer.core.data.dashboard;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("com.jsh.wirelessexplorer.core.common.IoDispatcher")
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
public final class TrafficStatsProvider_Factory implements Factory<TrafficStatsProvider> {
  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  private TrafficStatsProvider_Factory(Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public TrafficStatsProvider get() {
    return newInstance(ioDispatcherProvider.get());
  }

  public static TrafficStatsProvider_Factory create(
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new TrafficStatsProvider_Factory(ioDispatcherProvider);
  }

  public static TrafficStatsProvider newInstance(CoroutineDispatcher ioDispatcher) {
    return new TrafficStatsProvider(ioDispatcher);
  }
}
