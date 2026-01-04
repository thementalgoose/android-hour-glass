import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class HourglassAndroidJunit5ConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply JUnit5 plugin
            pluginManager.apply("de.mannodermaus.android-junit5")

            // Configure Android extension
            extensions.configure<LibraryExtension> {
                // Source sets
                sourceSets {
                    getByName("main") {
                        java.srcDirs("src/main/kotlin")
                    }
                    getByName("androidTest") {
                        java.srcDirs("src/androidTest/kotlin")
                        resources.srcDirs("src/androidTest/res")
                    }
                    getByName("test") {
                        java.srcDirs("src/test/java")
                    }
                }

                // Test options
                testOptions {
                    unitTests {
                        // Include Android resources if test directory exists
                        isIncludeAndroidResources = layout.projectDirectory.dir("src/test").asFile.exists()
                        isReturnDefaultValues = true

                        all {
                            it.afterSuite(KotlinClosure2<Any, Any, Unit>({ descriptor, result ->
                                @Suppress("UNCHECKED_CAST")
                                val desc = descriptor as org.gradle.api.tasks.testing.TestDescriptor
                                @Suppress("UNCHECKED_CAST")
                                val res = result as org.gradle.api.tasks.testing.TestResult

                                if (desc.parent == null) {
                                    println("\n=======================================================================")
                                    println(desc.displayName)
                                    println("Test result: ${res.resultType}")
                                    println("Test summary: ${res.testCount} tests, " +
                                            "${res.successfulTestCount} succeeded, " +
                                            "${res.failedTestCount} failed, " +
                                            "${res.skippedTestCount} skipped")
                                    println("=======================================================================")
                                }
                            }))

                            it.useJUnitPlatform()
                            (it as Test).testLogging {
                                showStandardStreams = true
                                events("passed", "skipped", "failed", "standardOut", "standardError")
                            }
                        }
                    }
                }
            }

            // Configure test tasks
            tasks.withType<Test> {
                testLogging {
                    exceptionFormat = TestExceptionFormat.FULL
                    showCauses = true
                    showExceptions = true
                    showStackTraces = true
                    showStandardStreams = true
                }
            }

            // Add test dependencies
            dependencies {
                add("testImplementation", libs.findLibrary("junit5.api").get())
                add("testImplementation", libs.findLibrary("junit5.engine").get())
                add("testImplementation", libs.findLibrary("junit5.params").get())
                add("testRuntimeOnly", libs.findLibrary("junit5.engine").get())

                add("testImplementation", libs.findLibrary("mockk").get())
                add("testImplementation", libs.findLibrary("tmg.testutils").get())
                add("testImplementation", libs.findLibrary("turbine").get())

                add("testImplementation", libs.findLibrary("test.androidx.core").get())
                add("testImplementation", libs.findLibrary("test.androidx.arch").get())
                add("kspTest", libs.findLibrary("test.androidx.lifecycle").get())
            }
        }
    }
}

// Helper class for Kotlin closure compatibility with Groovy API
class KotlinClosure2<in T1, in T2, R>(
    private val function: (T1, T2) -> R
) : groovy.lang.Closure<R>(null) {
    @Suppress("unused")
    fun doCall(arg1: T1, arg2: T2): R = function(arg1, arg2)
}
