plugins {
    id("wirelessexplorer.android.feature")
}

android {
    namespace = "com.jsh.wirelessexplorer.feature.settings"
    buildFeatures.buildConfig = true
    defaultConfig.buildConfigField("String", "VERSION_NAME", "\"1.0.0\"")
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
}
