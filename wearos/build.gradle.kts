plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.composecompiler)
    alias(libs.plugins.jetbrains.kotlin.serialization)
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
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.google.play.services.wearable)
    implementation(libs.androidx.wear)
    implementation(libs.androidx.wear.watchface.complications.data.source)
    implementation(libs.androidx.wear.watchface.complications.data)

    implementation(libs.bundles.kotlin)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material)
    implementation(libs.compose.activity)
}
