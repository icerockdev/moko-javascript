plugins {
    `kotlin-dsl`
}

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
    implementation(libs.mokoMultiplatform)
    implementation(libs.kotlinGradlePlugin)
    implementation(libs.androidGradlePlugin)
    implementation(libs.detektPlugin)
}
