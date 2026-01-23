plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit5")
    id("hourglass.android.compose")
}

android {
    namespace = "tmg.hourglass.widgets"
}

dependencies {
    implementation(libs.bundles.androidx.glance)

    implementation(project(":domain"))
    implementation(project(":presentation:strings"))
    implementation(project(":presentation:ui"))

    testImplementation(testFixtures(project(":domain")))
}
