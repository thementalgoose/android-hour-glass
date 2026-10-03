plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit6")
    id("hourglass.android.compose")
}

android {
    namespace = "tmg.hourglass.presentation"
}

dependencies {
    implementation(project(":presentation:strings"))
}
