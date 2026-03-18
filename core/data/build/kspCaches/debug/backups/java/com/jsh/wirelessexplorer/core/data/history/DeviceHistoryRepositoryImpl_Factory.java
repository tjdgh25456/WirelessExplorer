package com.jsh.wirelessexplorer.core.data.history;

import com.jsh.wirelessexplorer.core.database.dao.DeviceHistoryDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata
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
public final class DeviceHistoryRepositoryImpl_Factory implements Factory<DeviceHistoryRepositoryImpl> {
  private final Provider<DeviceHistoryDao> daoProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  private DeviceHistoryRepositoryImpl_Factory(Provider<DeviceHistoryDao> daoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.daoProvider = daoProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public DeviceHistoryRepositoryImpl get() {
    return newInstance(daoProvider.get(), ioDispatcherProvider.get());
  }

  public static DeviceHistoryRepositoryImpl_Factory create(Provider<DeviceHistoryDao> daoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new DeviceHistoryRepositoryImpl_Factory(daoProvider, ioDispatcherProvider);
  }

  public static DeviceHistoryRepositoryImpl newInstance(DeviceHistoryDao dao,
      CoroutineDispatcher ioDispatcher) {
    return new DeviceHistoryRepositoryImpl(dao, ioDispatcher);
  }
}
