plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit6")
    id("hourglass.android.compose")
}

android {
    namespace = "tmg.hourglass.core.googleanalytics"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
}
