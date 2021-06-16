/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class JavaScriptEngineJsonTests {
    private lateinit var javaScriptEngine: JavaScriptEngine
    private lateinit var context: Map<String, JsType>

    @BeforeTest
    fun init() {
        javaScriptEngine = JavaScriptEngine()

        val form = mapOf("selector_1" to "first_value", "selector_2" to "second_value")
        val profile = mapOf("email" to "test@test.com")
        val formJson = Json.encodeToJsonElement(form)
        val profileJson = Json.encodeToJsonElement(profile)

        context = mapOf(
            "form" to JsType.Json(formJson),
            "profile" to JsType.Json(profileJson)
        )
    }

    @AfterTest
    fun dispose() {
        javaScriptEngine.close()
    }

    @Test
    fun formSelectorCheck() {
        assertEquals(
            expected = JsType.Bool(true),
            actual = javaScriptEngine.evaluate(
                context = context,
                script = "form.selector_1 == \"first_value\""
            )
        )
    }

    @Test
    fun profileEmailCheck() {
        assertEquals(
            expected = JsType.Bool(true),
            actual = javaScriptEngine.evaluate(context = context, script = "profile.email != null")
        )
    }

    @Test
    fun profileStringGet() {
        assertEquals(
            expected = JsType.Str("test@test.com"),
            actual = javaScriptEngine.evaluate(context = context, script = "profile.email")
        )
    }

    @Test
    fun profileNullGet() {
        assertEquals(
            expected = JsType.Null,
            actual = javaScriptEngine.evaluate(context = context, script = "profile.first_name")
        )
    }
}
