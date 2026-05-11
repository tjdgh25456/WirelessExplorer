plugins {
    id("wirelessexplorer.jvm.library")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
}
