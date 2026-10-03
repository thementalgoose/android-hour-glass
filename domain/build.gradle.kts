plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit6")
}

android {
    namespace = "tmg.hourglass.domain"

    buildFeatures {
        testFixtures.enable = true
    }
}

dependencies {
    implementation(project(":presentation:strings"))
}
