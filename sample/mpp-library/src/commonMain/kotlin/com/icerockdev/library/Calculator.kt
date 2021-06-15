/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import dev.icerock.moko.javascript.JavaScriptEngine
import dev.icerock.moko.javascript.JsType

class Calculator {
    fun run(a: String, b: String): JsType {
        val engine = JavaScriptEngine()
        val testScript = "a+b"

        val intA = a.toIntOrNull()
        val intB = b.toIntOrNull()

        val context = if (intA != null && intB != null) {
            mapOf(
                "a" to JsType.IntNum(intA),
                "b" to JsType.IntNum(intB)
            )
        } else {
            mapOf(
                "a" to JsType.Str(a),
                "b" to JsType.Str(b)
            )
        }

        return engine.evaluate(
            context = context,
            script = testScript
        )
    }
}
