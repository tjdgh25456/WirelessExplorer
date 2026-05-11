plugins {
    id("wirelessexplorer.android.library")
}

android {
    namespace = "com.jsh.wirelessexplorer.core.testing"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(libs.junit)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.turbine)
    implementation(libs.mockk)
}
