/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

object Deps {
    private const val kotlinTestVersion = "1.4.32"

    private const val androidAppCompatVersion = "1.1.0"
    private const val materialDesignVersion = "1.0.0"
    private const val androidLifecycleVersion = "2.1.0"
    private const val androidCoreTestingVersion = "2.1.0"
    private const val quickjsVersion = "0.9.0"

    private const val coroutinesVersion = "1.4.2"
    private const val kotlinxSerializationVersion = "1.1.0"
    private const val mokoTestVersion = "0.2.0"
    const val mokoJavascriptVersion = "0.1.0"

    object Android {
        const val compileSdk = 30
        const val targetSdk = 30
        const val minSdk = 16
    }

    object Libs {
        object Android {
            const val appCompat = "androidx.appcompat:appcompat:$androidAppCompatVersion"
            const val material = "com.google.android.material:material:$materialDesignVersion"
            const val lifecycle = "androidx.lifecycle:lifecycle-extensions:$androidLifecycleVersion"

            const val quickjs = "app.cash.quickjs:quickjs-android:$quickjsVersion"
        }

        object MultiPlatform {
            const val coroutines =
                "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val kotlinSerialization =
                "org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion"

            const val mokoTest = "dev.icerock.moko:test:$mokoTestVersion"
            const val mokoJavascript = "dev.icerock.moko:javascript:$mokoJavascriptVersion"
        }
    }
}
