plugins {
    id("wirelessexplorer.android.library.compose")
}

android {
    namespace = "com.jsh.wirelessexplorer.core.ui"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    implementation(libs.accompanist.permissions)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.androidx.lifecycle.runtime.compose)
}
