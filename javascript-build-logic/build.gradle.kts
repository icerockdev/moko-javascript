plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    api("dev.icerock:mobile-multiplatform:0.14.1")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.10")
    api("com.android.tools.build:gradle:8.12.3")
    api("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.15.0")
}
