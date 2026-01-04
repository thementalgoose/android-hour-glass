plugins {
    id("hourglass.android.library")
}

android {
    namespace = "tmg.hourglass.core.crashlytics"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
