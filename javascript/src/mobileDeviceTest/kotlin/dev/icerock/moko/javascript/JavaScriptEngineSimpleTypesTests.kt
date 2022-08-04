/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

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

    //    @Ignore // on iOS we got StrValue, on Android - JsonValue
    @Test
    fun jsonReadCheck() {
        val jsonObject: JsonElement = javaScriptEngine.evaluate(
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
                    var obj = {male:"Ford", model:"Mustang", number:10};
                    Object.assign(obj, test);
                    obj
                """.trimIndent()
        ).jsonValue()
        assertTrue { jsonObject is JsonObject }
        assertEquals(
            expected = """{"number":10.0,"model":"Mustang","te":["tetete"],"male":"Ford"}""",
            actual = jsonObject.toString()
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
