plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit6")
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "tmg.hourglass.backup"
}

dependencies {
    implementation(project(":domain"))
}
