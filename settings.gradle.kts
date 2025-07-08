pluginManagement {
    includeBuild("build-logic") // include build-logic module
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// add support for type-safe project accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Jubro"
include(":app")
