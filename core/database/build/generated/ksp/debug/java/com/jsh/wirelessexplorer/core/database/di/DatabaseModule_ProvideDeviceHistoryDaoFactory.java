package com.jsh.wirelessexplorer.core.database.di;

import com.jsh.wirelessexplorer.core.database.AppDatabase;
import com.jsh.wirelessexplorer.core.database.dao.DeviceHistoryDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideDeviceHistoryDaoFactory implements Factory<DeviceHistoryDao> {
  private final Provider<AppDatabase> databaseProvider;

  private DatabaseModule_ProvideDeviceHistoryDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DeviceHistoryDao get() {
    return provideDeviceHistoryDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideDeviceHistoryDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideDeviceHistoryDaoFactory(databaseProvider);
  }

  public static DeviceHistoryDao provideDeviceHistoryDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDeviceHistoryDao(database));
  }
}
