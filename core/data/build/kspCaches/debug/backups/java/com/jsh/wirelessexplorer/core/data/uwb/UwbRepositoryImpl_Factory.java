package com.jsh.wirelessexplorer.core.data.uwb;

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
public final class UwbRepositoryImpl_Factory implements Factory<UwbRepositoryImpl> {
  private final Provider<UwbRanger> uwbRangerProvider;

  private UwbRepositoryImpl_Factory(Provider<UwbRanger> uwbRangerProvider) {
    this.uwbRangerProvider = uwbRangerProvider;
  }

  @Override
  public UwbRepositoryImpl get() {
    return newInstance(uwbRangerProvider.get());
  }

  public static UwbRepositoryImpl_Factory create(Provider<UwbRanger> uwbRangerProvider) {
    return new UwbRepositoryImpl_Factory(uwbRangerProvider);
  }

  public static UwbRepositoryImpl newInstance(UwbRanger uwbRanger) {
    return new UwbRepositoryImpl(uwbRanger);
  }
}
