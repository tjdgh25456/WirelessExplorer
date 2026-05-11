plugins {
    id("wirelessexplorer.android.feature")
}

android {
    namespace = "com.jsh.wirelessexplorer.feature.onboarding"
}

dependencies {
    implementation(libs.accompanist.permissions)
}
