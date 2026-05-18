plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit5")
    id("hourglass.android.compose")
}

android {
    namespace = "tmg.hourglass.wearos"
}

dependencies {
    implementation(libs.androidx.wearos)

    implementation(project(":domain"))
    implementation(project(":presentation:strings"))

    testImplementation(testFixtures(project(":domain")))
}
