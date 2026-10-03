plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit6")
    id("hourglass.android.compose")
}

android {
    namespace = "tmg.hourglass.prefs"
}

dependencies {
    implementation(project(":domain"))
}
