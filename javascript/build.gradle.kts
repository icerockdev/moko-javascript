/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("multiplatform-library-convention")
    id("dev.icerock.mobile.multiplatform.android-manifest")
    id("publication-convention")
}

android {
    testOptions.unitTests.isIncludeAndroidResources = true
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getByName("androidTest").java.srcDirs(
            file("src/androidAndroidTest/kotlin"),
            file("src/mobileDeviceTest/kotlin")
        )
    }
}

dependencies {
    androidMainImplementation(libs.quickjs)
    commonMainImplementation(libs.kotlinSerialization)
    commonTestImplementation(libs.kotlinTest)
    commonTestImplementation(libs.kotlinTestAnnotations)
    commonTestImplementation(libs.mokoTest)
    androidTestImplementation(libs.kotlinTestJUnit)
    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.testRules)
    androidTestImplementation(libs.testJUnitExt)
    androidTestImplementation(libs.testJUnitExtKtx)
}

tasks.withType<AbstractTestTask> {
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = setOf(
            TestLogEvent.SKIPPED,
            TestLogEvent.PASSED,
            TestLogEvent.FAILED
        )
        showStandardStreams = true
    }
    outputs.upToDateWhen { false }
}
