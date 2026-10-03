plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit6")
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "tmg.hourglass.domain"

    buildFeatures {
        testFixtures.enable = true
    }
}

dependencies {
    implementation(project(":presentation:strings"))
    implementation(libs.bundles.kotlin)
}
