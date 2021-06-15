/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

sealed class JsType {
    data class Boolean(val value: Boolean): JsType
    data class String(val value: String): JsType
    data class Int(val value: Int): JsType
    data class Double(val value: Double): JsType
    data class Json(val value: JsonElement): JsType
}
