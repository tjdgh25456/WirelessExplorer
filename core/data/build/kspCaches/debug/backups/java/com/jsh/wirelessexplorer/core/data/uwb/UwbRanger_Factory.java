package com.jsh.wirelessexplorer.core.data.uwb;

import android.content.Context;
import com.jsh.wirelessexplorer.core.common.TimeProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class UwbRanger_Factory implements Factory<UwbRanger> {
  private final Provider<Context> contextProvider;

  private final Provider<TimeProvider> timeProvider;

  private UwbRanger_Factory(Provider<Context> contextProvider,
      Provider<TimeProvider> timeProvider) {
    this.contextProvider = contextProvider;
    this.timeProvider = timeProvider;
  }

  @Override
  public UwbRanger get() {
    return newInstance(contextProvider.get(), timeProvider.get());
  }

  public static UwbRanger_Factory create(Provider<Context> contextProvider,
      Provider<TimeProvider> timeProvider) {
    return new UwbRanger_Factory(contextProvider, timeProvider);
  }

  public static UwbRanger newInstance(Context context, TimeProvider timeProvider) {
    return new UwbRanger(context, timeProvider);
  }
}
