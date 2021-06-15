/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import kotlinx.serialization.json.JsonElement

sealed class JsType {
    data class Bool(val value: Boolean): JsType()
    data class Str(val value: String): JsType()
    data class IntNum(val value: Int): JsType()
    data class DoubleNum(val value: Double): JsType()
    data class Json(val value: JsonElement): JsType()

    /**
     * For "undefined" and "null".
     */
    object Null : JsType()
}

fun JsType.boolValue(): Boolean = (this as JsType.Bool).value
fun JsType.stringValue(): String = (this as JsType.Str).value
fun JsType.intValue(): Int = (this as JsType.IntNum).value
fun JsType.doubleValue(): Double = (this as JsType.DoubleNum).value
fun JsType.jsonValue(): JsonElement = (this as JsType.Json).value
fun JsType.nullValue(): Any? = (this as JsType.Null).let { null }
