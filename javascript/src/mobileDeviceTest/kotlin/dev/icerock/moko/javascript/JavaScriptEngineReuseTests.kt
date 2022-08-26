/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class JavaScriptEngineReuseTests {
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
    fun reuseContextTest() {
        javaScriptEngine.evaluate(
            context = emptyMap(),
            """
                var firstVal = "hello";
            """.trimIndent()
        )
        val result = javaScriptEngine.evaluate(
            context = mapOf("secondVal" to JsType.Str("world")),
            """
                firstVal + " " + secondVal
            """.trimIndent()
        )

        assertEquals(
            expected = JsType.Str("hello world"),
            actual = result
        )
    }

    @Test
    fun reuseGlobalContextTest() {
        javaScriptEngine.setContextObjects(
            "myTest" to JsType.Str("global")
        )
        javaScriptEngine.evaluate(
            context = emptyMap(),
            """
                var firstVal = "hello";
            """.trimIndent()
        )
        val result = javaScriptEngine.evaluate(
            context = mapOf("secondVal" to JsType.Str("world")),
            """
                firstVal + " " + myTest + " " + secondVal
            """.trimIndent()
        )

        assertEquals(
            expected = JsType.Str("hello global world"),
            actual = result
        )
    }
}
