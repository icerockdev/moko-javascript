/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("mpp-library-convention")
    id("publication-convention")
}

android {
    testOptions.unitTests.isIncludeAndroidResources = true
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")

    }
}

dependencies {
    androidMainImplementation(Deps.Libs.Android.quickjs)

    commonMainImplementation(Deps.Libs.MultiPlatform.kotlinSerialization)

    commonTestImplementation(Deps.Libs.MultiPlatform.kotlinTest)
    commonTestImplementation(Deps.Libs.MultiPlatform.kotlinTestAnnotations)
    commonTestImplementation(Deps.Libs.MultiPlatform.mokoTest)

    androidTestImplementation(Deps.Libs.Android.testRunner)
    androidTestImplementation(Deps.Libs.Android.testRules)
    androidTestImplementation(Deps.Libs.Android.testJUnitExt)
    androidTestImplementation(Deps.Libs.Android.testJUnitExtKtx)
}
