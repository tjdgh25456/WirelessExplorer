plugins {
    id("wirelessexplorer.android.feature")
}

android {
    namespace = "com.jsh.wirelessexplorer.feature.devices"
}

dependencies {
    implementation(projects.feature.devicesBle)
    implementation(projects.feature.devicesWifi)
    implementation(projects.feature.devicesNfc)
    implementation(projects.feature.devicesUwb)
}
