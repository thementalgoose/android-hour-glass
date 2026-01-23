pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "android-hour-glass"

include(":app")
include(":core:metrics:googleanalytics")
include(":core:metrics:crashlytics")
include(":domain")
include(":data:room")
include(":presentation:ui")
include(":presentation:strings")
include(":widgets")
