plugins {
    id("wirelessexplorer.android.feature")
}

android {
    namespace = "com.jsh.wirelessexplorer.feature.analyze"
}

dependencies {
    implementation(libs.vico.compose)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
}
