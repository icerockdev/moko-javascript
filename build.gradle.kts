/*
 * Copyright 2025 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */
apply(plugin = "dev.icerock.moko.gradle.publication.nexus")

buildscript {
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
    dependencies {
        classpath(libs.mokoGradlePlugin)
        classpath(":javascript-build-logic")
    }
}

val mokoVersion = moko.versions.mokoJavascriptVersion.get()
allprojects {
    group = "dev.icerock.moko"
    version = mokoVersion
}

tasks.register("clean", Delete::class).configure {
    group = "build"
    delete(rootProject.layout.buildDirectory)
}
