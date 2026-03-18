plugins {
    id("wirelessexplorer.android.library")
    id("wirelessexplorer.kotlin.serialization")
}

android {
    namespace = "com.jsh.wirelessexplorer.core.navigation"
}

dependencies {
    implementation(libs.navigation.compose)
    implementation(libs.compose.material.icons.extended)
}
