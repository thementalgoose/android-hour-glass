import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.composecompiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.junit6)
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

kotlin {
    jvmToolchain(25)
}

android {
    namespace = "tmg.hourglass.wearos"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "tmg.hourglass"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = versionCodeProperty
        versionName = versionNameProperty
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".sandbox"
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = true
            }
        }
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.google.play.services.wearable)
    implementation(libs.androidx.wear)
    implementation(libs.androidx.wear.watchface.complications.data.source)
    implementation(libs.androidx.wear.watchface.complications.data)

    implementation(libs.androidx.wear.compose.foundation)
    implementation(libs.androidx.wear.compose.material)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.kapt.compiler)
    implementation(libs.hilt.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    implementation(libs.bundles.kotlin)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material)
    implementation(libs.compose.activity)
    implementation(libs.compose.ui.toolingpreview)
    debugImplementation(libs.compose.ui.tooling)

    testImplementation(libs.junit6.api)
    testRuntimeOnly(libs.junit6.engine)
}
