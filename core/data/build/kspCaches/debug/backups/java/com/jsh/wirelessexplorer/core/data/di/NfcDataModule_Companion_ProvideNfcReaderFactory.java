package com.jsh.wirelessexplorer.core.data.di;

import android.content.Context;
import com.jsh.wirelessexplorer.core.data.nfc.NfcReader;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata({
    "dagger.hilt.android.qualifiers.ApplicationContext",
    "com.jsh.wirelessexplorer.core.common.IoDispatcher"
})
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
public final class NfcDataModule_Companion_ProvideNfcReaderFactory implements Factory<NfcReader> {
  private final Provider<Context> contextProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  private NfcDataModule_Companion_ProvideNfcReaderFactory(Provider<Context> contextProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.contextProvider = contextProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public NfcReader get() {
    return provideNfcReader(contextProvider.get(), ioDispatcherProvider.get());
  }

  public static NfcDataModule_Companion_ProvideNfcReaderFactory create(
      Provider<Context> contextProvider, Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new NfcDataModule_Companion_ProvideNfcReaderFactory(contextProvider, ioDispatcherProvider);
  }

  public static NfcReader provideNfcReader(Context context, CoroutineDispatcher ioDispatcher) {
    return Preconditions.checkNotNullFromProvides(NfcDataModule.Companion.provideNfcReader(context, ioDispatcher));
  }
}
