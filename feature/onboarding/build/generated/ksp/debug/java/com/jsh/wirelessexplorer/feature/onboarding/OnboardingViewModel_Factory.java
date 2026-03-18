package com.jsh.wirelessexplorer.feature.onboarding;

import com.jsh.wirelessexplorer.core.domain.settings.CompleteOnboardingUseCase;
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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<CompleteOnboardingUseCase> completeOnboardingProvider;

  private OnboardingViewModel_Factory(
      Provider<CompleteOnboardingUseCase> completeOnboardingProvider) {
    this.completeOnboardingProvider = completeOnboardingProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(completeOnboardingProvider.get());
  }

  public static OnboardingViewModel_Factory create(
      Provider<CompleteOnboardingUseCase> completeOnboardingProvider) {
    return new OnboardingViewModel_Factory(completeOnboardingProvider);
  }

  public static OnboardingViewModel newInstance(CompleteOnboardingUseCase completeOnboarding) {
    return new OnboardingViewModel(completeOnboarding);
  }
}
