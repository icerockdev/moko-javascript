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

        val numberA = a.toDoubleOrNull()
        val numberB = b.toDoubleOrNull()

        val context = if (numberA != null && numberB != null) {
            mapOf(
                "a" to JsType.DoubleNum(numberA),
                "b" to JsType.DoubleNum(numberB)
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
