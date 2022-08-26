/*
 * Copyright 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

internal class ContextProviderDynamic : ContextProvider {
    var context: Map<String, JsType> = emptyMap()
    var activeScript: String = ""

    override fun getBool(name: String): Boolean {
        return context[name]!!.boolValue()
    }

    override fun getDouble(name: String): Double {
        return context[name]!!.doubleValue()
    }

    override fun getString(name: String): String {
        val jsType: JsType = context[name]!!
        return when (jsType) {
            is JsType.Bool, is JsType.DoubleNum, JsType.Null -> throw IllegalArgumentException()
            is JsType.Json -> jsType.value.toString()
            is JsType.Str -> jsType.value
        }
    }

    override fun getScript(): String = activeScript
}
