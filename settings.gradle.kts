/*
 * Copyright 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }

    versionCatalogs {
        create("moko") {
            from(files("gradle/moko.versions.toml"))
        }
    }
}

rootProject.name = "moko-javascript"

if (gradle.parent == null) {
    includeBuild("javascript-build-logic")
}

include(":javascript")
include(":sample:android-app")
include(":sample:mpp-library")
