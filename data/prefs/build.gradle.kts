plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit5")
    id("hourglass.android.compose")
}

android {
    namespace = "tmg.hourglass.prefs"
}

dependencies {
    implementation(project(":domain"))
}
