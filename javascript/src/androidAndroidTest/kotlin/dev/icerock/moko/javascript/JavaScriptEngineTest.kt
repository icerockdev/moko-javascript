/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JavaScriptEngineTest {
    private var _javaScriptEngine: JavaScriptEngine? = null

    private val javaScriptEngine: JavaScriptEngine
        get() = _javaScriptEngine!!

    @Before
    fun init() {
        _javaScriptEngine = JavaScriptEngine()
    }

    @After
    fun dispose() {
        javaScriptEngine.close()
    }

    @Test
    fun evaluationTest() {
        assertEquals(
            expected = 101,
            actual = javaScriptEngine.evaluate(
                context = emptyMap(),
                script = """
                    var a = 1 + 100;
                    a
                """.trimIndent()
            ).intValue()
        )

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

        assertTrue {
            javaScriptEngine.evaluate(
                context = emptyMap(),
                script = """
                    var obj = {male:"Ford", model:"Mustang", number:10};
                    JSON.stringify(obj);
                """.trimIndent()
            ).jsonValue() is JsonObject
        }

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

    @Test
    fun `testJsonContext`() {
        val form = mapOf("selector_1" to "first_value", "selector_2" to "second_value")
        val profile = mapOf("email" to "test@test.com")
        val formJson = Json.encodeToJsonElement(form)
        val profileJson = Json.encodeToJsonElement(profile)
        val context: Map<String, JsType> = mapOf(
            "form" to JsType.Json(formJson),
            "profile" to JsType.Json(profileJson)
        )

        assertEquals(
            expected = JsType.Bool(true),
            actual = javaScriptEngine.evaluate(
                context = context,
                script = "form.selector_1 == \"first_value\""
            )
        )

        assertEquals(
            expected = JsType.Bool(true),
            actual = javaScriptEngine.evaluate(context = context, script = "profile.email != null")
        )

        assertEquals(
            expected = JsType.Str("test@test.com"),
            actual = javaScriptEngine.evaluate(context = context, script = "profile.email")
        )

        assertEquals(
            expected = JsType.Null,
            actual = javaScriptEngine.evaluate(context = context, script = "profile.first_name")
        )
    }

    @Test
    fun testPlusScript() {
        val list = listOf(5, 15)
        val listJson = Json.encodeToJsonElement(list)
        val context: Map<String, JsType> = mapOf(
            "list" to JsType.Json(listJson),
            "number" to JsType.IntNum(4),
            "doubleString" to JsType.Str("\" Hello \"")
        )

        // On iOS got JsType.DoubleNum here
        assertEquals(
            expected = JsType.IntNum(19),
            actual = javaScriptEngine.evaluate(context = context, script = "list[1]+number")
        )

        // FIXME: Falls with exception
        // Script: var list = [5,15];var number = 4;var doubleString = " Hello ";doubleString+list[0]
        // Exception:
        // IllegalStateException: Reader has not consumed the whole input:
        // JsonReader(source=' Hello 5', currentPosition=8, tokenClass=0, tokenPosition=7, offset=7)
//        assertEquals(
//            actual = JsType.Str(" Hello 5"),
//            expected = javaScriptEngine.evaluate(context = context, script = "doubleString+list[0]")
//        )
    }
}
