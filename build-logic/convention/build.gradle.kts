plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.gradle.agp)
    compileOnly(libs.gradle.kotlin)
    compileOnly(libs.gradle.hilt)
    compileOnly(libs.gradle.junit5)
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
        register("hourglassAndroidJunit5") {
            id = "hourglass.android.junit5"
            implementationClass = "HourglassAndroidJunit5ConventionPlugin"
        }
    }
}
