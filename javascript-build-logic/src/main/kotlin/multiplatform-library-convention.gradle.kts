/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("android-base-convention")
    id("dev.icerock.mobile.multiplatform.android-manifest")
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    androidTarget {
        publishLibraryVariants("release", "debug")
    }

    sourceSets {
        val mobileDeviceTest by creating

        val commonMain by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        iosMain.dependsOn(commonMain)

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting

        val iosTest by creating {
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
        val androidUnitTest by getting

        val commonTest by getting

        mobileDeviceTest.dependsOn(commonTest)
        iosTest.dependsOn(mobileDeviceTest)
        androidUnitTest.dependsOn(mobileDeviceTest)
    }
}

