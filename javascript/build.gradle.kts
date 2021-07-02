/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("multiplatform-library-convention")
    id("dev.icerock.mobile.multiplatform.android-manifest")
    id("publication-convention")
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
