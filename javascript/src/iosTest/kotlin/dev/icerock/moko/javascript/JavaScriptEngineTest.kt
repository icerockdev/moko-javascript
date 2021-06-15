/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

class JavaScriptEngineTest {
    @Test
    fun `test json context`() {
        val form = mapOf("selector_1" to "first_value", "selector_2" to "second_value")
        val profile = mapOf("email" to "test@test.com")
        val formJson = Json.encodeToJsonElement(form)
        val profileJson = Json.encodeToJsonElement(profile)
        val context: Map<String, JsType> = mapOf("form" to JsType.Json(formJson), "profile" to JsType.Json(profileJson))

        val jsEngine = JavaScriptEngine()

        assertEquals(JsType.Bool(true), jsEngine.evaluate(context = context, script = "form.selector_1 == \"first_value\""))

        assertEquals(JsType.Bool(true), jsEngine.evaluate(context = context, script = "profile.email != null"))

        assertEquals(JsType.Str("test@test.com"), jsEngine.evaluate(context = context, script = "profile.email"))

        assertEquals(JsType.Null, jsEngine.evaluate(context = context, script = "profile.first_name"))
    }

    @Test
    fun `test plus script`() {
        val list = listOf<Int>(5, 15)
        val listJson = Json.encodeToJsonElement(list)
        val context: Map<String, JsType> = mapOf("list" to JsType.Json(listJson), "number" to JsType.IntNum(4), "doubleString" to JsType.Str(" Hello "))

        val jsEngine = JavaScriptEngine()

        assertEquals(JsType.DoubleNum(19.0), jsEngine.evaluate(context = context, script = "list[1]+number"))

        assertEquals(JsType.Str(" Hello 5"), jsEngine.evaluate(context = context, script = "doubleString+list[0]"))
    }
}
