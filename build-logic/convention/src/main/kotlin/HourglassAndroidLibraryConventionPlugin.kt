import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class HourglassAndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply plugins
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.android")
            pluginManager.apply("org.jetbrains.kotlin.plugin.parcelize")
            pluginManager.apply("com.google.devtools.ksp")
            pluginManager.apply("dagger.hilt.android.plugin")

            // Configure Android extension
            extensions.configure<LibraryExtension> {
                // SDK versions
                compileSdk = 36

                defaultConfig {
                    minSdk = 26
                    targetSdk = 36

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }

                buildFeatures {
                    buildConfig = true
                }

                buildTypes {
                    getByName("debug") {
                        isMinifyEnabled = false
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_21
                    targetCompatibility = JavaVersion.VERSION_21
                }

                lint {
                    disable.add("Instantiatable")
                }
            }

            // Configure Kotlin options
            extensions.configure<KotlinAndroidProjectExtension> {
                jvmToolchain(21)

                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_21)

                    // Kotlin compiler arguments
                    freeCompilerArgs.addAll(
                        "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        "-Xopt-in=kotlinx.coroutines.ObsoleteCoroutinesApi",
                        "-Xopt-in=kotlinx.coroutines.FlowPreview",
                        "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
                        "-Xopt-in=androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi"
                    )
                }
            }

            // Add dependencies
            dependencies {
                add("implementation", libs.findBundle("androidx").get())

                // Hilt
                add("implementation", libs.findLibrary("hilt.android").get())
                add("ksp", libs.findLibrary("hilt.compiler").get())
                add("ksp", libs.findLibrary("hilt.kapt.compiler").get())
                add("implementation", libs.findLibrary("hilt.compose").get())

                // Core
                add("implementation", libs.findBundle("kotlin").get())

                // Test
                add("testImplementation", libs.findBundle("kotlin").get())
                add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())

                // Utilities
                add("implementation", libs.findLibrary("tmg.utils").get())
                add("testImplementation", libs.findLibrary("tmg.testutils").get())
            }
        }
    }
}
