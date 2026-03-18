pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "WirelessExplorer"

include(":app")

// Core (10)
include(":core:common")
include(":core:model")
include(":core:domain")
include(":core:data")
include(":core:designsystem")
include(":core:ui")
include(":core:navigation")
include(":core:database")
include(":core:datastore")
include(":core:testing")

// Feature (12)
include(":feature:dashboard")
include(":feature:devices")
include(":feature:devices-ble")
include(":feature:devices-wifi")
include(":feature:devices-nfc")
include(":feature:devices-uwb")
include(":feature:detail")
include(":feature:history")
include(":feature:search")
include(":feature:analyze")
include(":feature:settings")
include(":feature:onboarding")
