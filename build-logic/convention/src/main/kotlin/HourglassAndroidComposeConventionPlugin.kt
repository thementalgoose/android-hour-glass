import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class HourglassAndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply compose compiler plugin
            val composeCompilerPlugin = libs.findPlugin("composecompiler").get().get().pluginId
            pluginManager.apply(composeCompilerPlugin)

            // Configure Android extension
            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
            }

            // Add Compose dependencies
            dependencies {
                val composeBom = libs.findLibrary("compose.bom").get()
                add("implementation", platform(composeBom))

                add("implementation", libs.findLibrary("compose.material").get())
                add("implementation", libs.findLibrary("compose.material3").get())
                add("implementation", libs.findLibrary("compose.material3icons").get())
                add("implementation", libs.findLibrary("compose.windowsize").get())
                add("implementation", libs.findLibrary("compose.animation").get())
                add("debugImplementation", libs.findLibrary("compose.ui.tooling").get())
                add("implementation", libs.findLibrary("compose.ui.toolingpreview").get())
                add("implementation", libs.findLibrary("compose.ui.viewbinding").get())
                add("implementation", libs.findLibrary("compose.livedata").get())

                add("implementation", libs.findLibrary("compose.activity").get())
                add("implementation", libs.findLibrary("compose.theme.adapter").get())
                add("implementation", libs.findLibrary("compose.navigation").get())
                add("implementation", libs.findLibrary("compose.viewmodel").get())

                add("implementation", libs.findLibrary("androidx.window").get())
                add("implementation", libs.findLibrary("coil").get())

                add("implementation", libs.findBundle("accompanist").get())
            }
        }
    }
}
