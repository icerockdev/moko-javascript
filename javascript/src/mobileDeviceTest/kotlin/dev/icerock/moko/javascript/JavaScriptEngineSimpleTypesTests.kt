/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class JavaScriptEngineSimpleTypesTests {
    private lateinit var javaScriptEngine: JavaScriptEngine

    @BeforeTest
    fun init() {
        javaScriptEngine = JavaScriptEngine()
    }

    @AfterTest
    fun dispose() {
        javaScriptEngine.close()
    }

    @Test
    fun integerSumCheck() {
        assertEquals(
            expected = 101.0,
            actual = javaScriptEngine.evaluate(
                context = emptyMap(),
                script = """
                    var a = 1 + 100;
                    a
                """.trimIndent()
            ).doubleValue()
        )
    }

    @Test
    fun doubleSumCheck() {
        assertEquals(
            expected = 1.5,
            actual = javaScriptEngine.evaluate(
                context = emptyMap(),
                script = """
                    var a = 0.5 + 1.0;
                    a
                """.trimIndent()
            ).doubleValue()
        )
    }

    @Test
    fun stringReturnCheck() {
        assertEquals(
            expected = "string",
            actual = javaScriptEngine.evaluate(
                context = emptyMap(),
                script = """
                    var a = "string";
                    a
                """.trimIndent()
            ).stringValue()
        )
    }

    @Test
    fun booleanCheck() {
        assertEquals(
            expected = false,
            actual = javaScriptEngine.evaluate(
                context = emptyMap(),
                script = """
                    var a = false == true;
                    a
                """.trimIndent()
            ).boolValue()
        )
    }

    @Test
    fun jsonReadCheck() {
        val result: JsType = javaScriptEngine.evaluate(
            context = mapOf(
                "test" to JsType.Json(
                    JsonObject(
                        mapOf(
                            "te" to JsonArray(
                                listOf(JsonPrimitive("tetete"))
                            )
                        )
                    )
                )
            ),
            script = """
                    var object = {male:"Ford", model:"Mustang", number:10.1};
                    Object.assign(object, test);
                    object;
                """.trimIndent()
        )
        assertIs<JsType.Json>(result)
        assertEquals(
            expected = JsonObject(
                mapOf(
                    "number" to JsonPrimitive(10.1),
                    "model" to JsonPrimitive("Mustang"),
                    "te" to JsonArray(listOf(JsonPrimitive("tetete"))),
                    "male" to JsonPrimitive("Ford")
                )
            ).entries,
            actual = result.value.jsonObject.entries
        )
    }

    @Test
    fun undefinedCheck() {
        assertNull(
            javaScriptEngine.evaluate(
                context = emptyMap(),
                script = """
                    var a = undefined
                    a
                """.trimIndent()
            ).nullValue()
        )
    }
}
