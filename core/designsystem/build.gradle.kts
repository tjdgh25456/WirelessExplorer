plugins {
    id("wirelessexplorer.android.library.compose")
}

android {
    namespace = "com.jsh.wirelessexplorer.core.designsystem"
}

dependencies {
    implementation(libs.androidx.activity.compose)
}
