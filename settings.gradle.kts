/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()

        jcenter()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()

        jcenter()
    }
}

include(":javascript")
include(":sample:android-app")
include(":sample:mpp-library")
