import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class KotlinSerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        dependencies {
            add("implementation", libs.findLibrary("kotlinx-serialization-json").get())
        }
    }
}
