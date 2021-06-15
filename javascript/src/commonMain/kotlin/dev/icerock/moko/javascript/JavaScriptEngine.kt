/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

expect class JavaScriptEngine() {
    fun evaluate(context: Map<String, JsType>, script: String): JsType
}
