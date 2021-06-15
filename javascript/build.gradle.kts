/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    id("mpp-library-convention")
    id("publication-convention")
}

dependencies {
    androidMainImplementation(Deps.Libs.Android.quickjs)

    commonMainImplementation(Deps.Libs.MultiPlatform.kotlinSerialization)

    commonTestImplementation(Deps.Libs.MultiPlatform.mokoTest)
}
