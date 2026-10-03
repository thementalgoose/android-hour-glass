plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.gradle.agp)
    compileOnly(libs.gradle.kotlin)
    compileOnly(libs.gradle.hilt)
    compileOnly(libs.gradle.junit6)
}

gradlePlugin {
    plugins {
        register("hourglassAndroidLibrary") {
            id = "hourglass.android.library"
            implementationClass = "HourglassAndroidLibraryConventionPlugin"
        }
        register("hourglassAndroidCompose") {
            id = "hourglass.android.compose"
            implementationClass = "HourglassAndroidComposeConventionPlugin"
        }
        register("hourglassAndroidJunit6") {
            id = "hourglass.android.junit6"
            implementationClass = "HourglassAndroidJunit6ConventionPlugin"
        }
    }
}
