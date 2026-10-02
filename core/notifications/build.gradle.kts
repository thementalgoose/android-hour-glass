plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit5")
}

android {
    namespace = "tmg.hourglass.notifications"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":presentation:strings"))
}
