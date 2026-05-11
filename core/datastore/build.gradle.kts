plugins {
    id("wirelessexplorer.android.library")
    id("wirelessexplorer.android.hilt")
}

android {
    namespace = "com.jsh.wirelessexplorer.core.datastore"
}

dependencies {
    implementation(libs.datastore.preferences)
}
