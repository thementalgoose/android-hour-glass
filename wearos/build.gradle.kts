plugins {
    id("hourglass.android.library")
    id("hourglass.android.junit5")
    id("hourglass.android.compose")
}

android {
    namespace = "tmg.hourglass.wearos"
}

dependencies {

    // AndroidX
    implementation(libs.bundles.androidx)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.kapt.compiler)
    implementation(libs.hilt.compose)

    // Kotlin
    implementation(libs.bundles.kotlin)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3icons)
    implementation(libs.compose.windowsize)
    implementation(libs.compose.animation)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.toolingpreview)
    implementation(libs.compose.ui.viewbinding)
    implementation(libs.compose.livedata)
    implementation(libs.compose.activity)
    implementation(libs.compose.theme.adapter)
    implementation(libs.compose.navigation)
    implementation(libs.compose.viewmodel)
    implementation(libs.androidx.window)
    implementation(libs.coil)

    implementation(libs.androidx.wearos)

    implementation(project(":domain"))
    implementation(project(":data:room"))
    implementation(project(":presentation:strings"))

    testImplementation(testFixtures(project(":domain")))
}
