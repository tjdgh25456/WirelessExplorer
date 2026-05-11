import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = "wirelessexplorer.android.library.compose")
        apply(plugin = "wirelessexplorer.android.hilt")
        apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        dependencies {
            add("implementation", project(":core:model"))
            add("implementation", project(":core:domain"))
            add("implementation", project(":core:designsystem"))
            add("implementation", project(":core:ui"))
            add("implementation", project(":core:common"))
            add("implementation", project(":core:navigation"))

            add("implementation", libs.findLibrary("androidx-lifecycle-runtime-compose").get())
            add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
            add("implementation", libs.findLibrary("hilt-navigation-compose").get())
            add("implementation", libs.findLibrary("kotlinx-collections-immutable").get())
            add("implementation", libs.findLibrary("compose-material-icons-extended").get())
            add("implementation", libs.findLibrary("timber").get())
            add("implementation", libs.findLibrary("kotlinx-coroutines-core").get())
            add("implementation", libs.findLibrary("kotlinx-coroutines-android").get())
        }
    }
}
