plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit5")
    alias(libs.plugins.kotlin.kapt)
}

apply(plugin = "realm-android")

android {
    namespace = "tmg.hourglass.realm"

    packaging {
        resources.excludes.add("lib/arm64-v8a/librealm-jni.so")
    }
}

dependencies {
    implementation(project(":domain"))
}
