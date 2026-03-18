plugins {
    id("wirelessexplorer.android.library")
    id("wirelessexplorer.android.hilt")
    id("wirelessexplorer.android.room")
}

android {
    namespace = "com.jsh.wirelessexplorer.core.database"
}

dependencies {
    implementation(projects.core.model)
}
