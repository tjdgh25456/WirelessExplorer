plugins {
    id("wirelessexplorer.jvm.library")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    compileOnly("javax.inject:javax.inject:1")
}
