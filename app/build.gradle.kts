plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.junit5)
    alias(libs.plugins.composecompiler)
    alias(libs.plugins.oss.licenses)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.google.services)
}

val versionCodeProperty: Int = try {
    System.getenv("VERSION_CODE").toInt()
} catch (e: Exception) {
    1
}
val versionNameProperty: String = try {
    System.getenv("VERSION_NAME")
} catch (e: Exception) {
    "1.0.0"
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "tmg.hourglass"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = versionCodeProperty
        versionName = "${versionNameProperty}.${versionCodeProperty}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    lint {
        disable += "Instantiatable"
    }

    kotlin {
        jvmToolchain(21)
    }

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

    testOptions {
        unitTests {
            isIncludeAndroidResources = layout.projectDirectory.dir("src/test").asFile.exists()
            isReturnDefaultValues = true
            all {
                it.useJUnitPlatform()
                it.testLogging {
                    showStandardStreams = true
                    events("passed", "skipped", "failed", "standardOut", "standardError")
                }
            }
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("KEYSTORE") ?: "hour-glass.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEYSTORE_ALIAS")
            keyPassword = System.getenv("KEYSTORE_PASSWORD")
        }
    }

    bundle {
        language {
            // Specifies that the app bundle should not support
            // configuration APKs for language resources. These
            // resources are instead packaged with each base and
            // dynamic feature APK.
            enableSplit = false
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packaging {
        jniLibs {
            excludes += listOf("META-INF/*")
        }
        resources {
            excludes += listOf(
                "win32-x86-64/attach_hotspot_windows.dll",
                "win32-x86/attach_hotspot_windows.dll",
                "META-INF/LGPL2.1",
                "META-INF/AL2.0",
                "META-INF/licenses/ASM",
                "META-INF/*",
                "lib/arm64-v8a/librealm-jni.so"
            )
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("sand") {
            isDefault = true
            dimension = "version"
            applicationIdSuffix = ".sandbox"
            versionNameSuffix = "-sandbox"

            buildConfigField("int", "ENVIRONMENT", "1")
            firebaseCrashlytics {
                mappingFileUploadEnabled = false
            }
        }

        create("live") {
            dimension = "version"
            buildConfigField("int", "ENVIRONMENT", "0")
            firebaseCrashlytics {
                mappingFileUploadEnabled = true
            }
        }
    }

    namespace = "tmg.hourglass"
}

dependencies {
    implementation(project(":core:metrics:googleanalytics"))
    implementation(project(":core:metrics:crashlytics"))

    // Modules
    implementation(project(":data:realm"))
    implementation(project(":data:room"))
    implementation(project(":domain"))
    implementation(project(":presentation:ui"))
    implementation(project(":presentation:strings"))
    implementation(project(":widgets"))

    // AndroidX
    implementation(libs.bundles.androidx)
    implementation(libs.androidx.splashscreen)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.kapt.compiler)
    implementation(libs.hilt.compose)

    // Kotlin
    implementation(libs.bundles.kotlin)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3icons)
    implementation(libs.compose.windowsize)
    implementation(libs.compose.animation)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.toolingpreview)
    implementation(libs.compose.ui.viewbinding)
    implementation(libs.compose.livedata)
    implementation(libs.compose.activity)
    implementation(libs.compose.theme.adapter)
    implementation(libs.compose.navigation)
    implementation(libs.compose.viewmodel)
    implementation(libs.androidx.window)
    implementation(libs.coil)
    implementation(libs.bundles.accompanist)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.perf)

    // TMG
    implementation(libs.tmg.aboutthisapp)
    implementation(libs.tmg.utils)
    implementation(libs.tmg.labelledprogressbar)

    // BugShaker
    implementation(libs.falcon)

    // Accompanist
    implementation(libs.accompanist.systemuicontroller)

    ksp(libs.kotlin.metadata.jvm)

    // Test
    testImplementation(libs.bundles.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.tmg.testutils)
    testImplementation(libs.junit5.api)
    testImplementation(libs.junit5.engine)
    testImplementation(libs.junit5.params)
    testRuntimeOnly(libs.junit5.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.test.androidx.core)
    testImplementation(libs.test.androidx.arch)
    kspTest(libs.test.androidx.lifecycle)

    testImplementation(testFixtures(project(":domain")))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.ObsoleteCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview",
            "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
            "-Xopt-in=androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi"
        )
    }
}

tasks.withType<Test> {
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
        showStandardStreams = true
    }
}
