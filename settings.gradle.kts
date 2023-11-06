pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // For Android dependencies
        mavenCentral() // For Java and Kotlin dependencies
    }
}


rootProject.name = "AuroraSheetApp"
include(":app")
 