/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class JavaScriptEngineCrossTypesTests {
    private lateinit var javaScriptEngine: JavaScriptEngine
    private lateinit var context: Map<String, JsType>

    @BeforeTest
    fun init() {
        javaScriptEngine = JavaScriptEngine()

        val list = listOf(5, 15)
        val listJson = Json.encodeToJsonElement(list)

        context = mapOf(
            "list" to JsType.Json(listJson),
            "number" to JsType.DoubleNum(4.0),
            "doubleString" to JsType.Str(" Hello ")
        )
    }

    @AfterTest
    fun dispose() {
        javaScriptEngine.close()
    }

    @Test
    fun numberWithNumberSumCheck() {
        assertEquals(
            expected = JsType.DoubleNum(19.0),
            actual = javaScriptEngine.evaluate(context = context, script = "list[1]+number")
        )
    }

    @Test
    fun stringWithNumberSumCheck() {
        // FIXME: Falls with exception
        // Script: var list = [5,15];var number = 4;var doubleString = " Hello ";doubleString+list[0]
        // Exception:
        // IllegalStateException: Reader has not consumed the whole input:
        // JsonReader(source=' Hello 5', currentPosition=8, tokenClass=0, tokenPosition=7, offset=7)
        assertEquals(
            actual = JsType.Str(" Hello 5"),
            expected = javaScriptEngine.evaluate(context = context, script = "doubleString+list[0]")
        )
    }
}
