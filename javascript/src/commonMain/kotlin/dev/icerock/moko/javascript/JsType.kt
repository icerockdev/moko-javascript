/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import kotlinx.serialization.json.JsonElement

sealed class JsType {
    data class Bool(override val value: Boolean) : JsType()
    data class Str(override val value: String) : JsType()
    data class DoubleNum(override val value: Double) : JsType()
    data class Json(override val value: JsonElement) : JsType()
    data class AnyValue(override val value: Any) : JsType()

    /**
     * For "undefined" and "null".
     */
    object Null : JsType() {
        override val value: Any? get() = null
    }

    abstract val value: Any?
}

fun JsType.boolValue(): Boolean = (this as JsType.Bool).value
fun JsType.stringValue(): String = (this as JsType.Str).value
fun JsType.doubleValue(): Double = (this as JsType.DoubleNum).value
fun JsType.jsonValue(): JsonElement = (this as JsType.Json).value
fun JsType.nullValue(): Any? = (this as JsType.Null).let { null }
