/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "moko-javascript"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }

    versionCatalogs {
        create("moko") {
            from(files("gradle/moko.versions.toml"))
        }
    }
}

if (gradle.parent == null) {
    includeBuild("javascript-build-logic")
}

include(":javascript")
include(":sample:android-app")
include(":sample:mpp-library")
