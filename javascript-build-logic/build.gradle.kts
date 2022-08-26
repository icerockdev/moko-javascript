plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()

    gradlePluginPortal()
}

dependencies {
    api("dev.icerock:mobile-multiplatform:0.14.1")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    api("com.android.tools.build:gradle:7.0.1")
    api("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.15.0")
}
