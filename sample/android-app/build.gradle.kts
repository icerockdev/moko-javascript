/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("android-app-convention")
    id("org.jetbrains.kotlin.android")
}

android {
    defaultConfig {
        applicationId = "dev.icerock.moko.samples.javascript"

        versionCode = 1
        versionName = "0.1.0"
    }
}

dependencies {
    implementation(Deps.Libs.Android.appCompat)
    implementation(Deps.Libs.Android.material)

    implementation(project(":sample:mpp-library"))
}
