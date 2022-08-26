/*
 * Copyright 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

internal interface ContextProvider {
    fun getBool(name: String): Boolean
    fun getDouble(name: String): Double

    fun getString(name: String): String

    fun getScript(): String
}
