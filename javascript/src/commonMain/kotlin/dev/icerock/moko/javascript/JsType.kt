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
