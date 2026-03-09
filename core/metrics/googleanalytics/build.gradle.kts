plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit5")
    id("hourglass.android.compose")
}

android {
    namespace = "tmg.hourglass.core.crashlytics"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
