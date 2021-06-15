/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import org.junit.After
import org.junit.Before
import org.junit.Test

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
    fun equalityTest() {
        val jsScript = """
            var a = 1 + 2;
            a
        """.trimIndent()
        val result = javaScriptEngine.evaluate(emptyMap(), jsScript)

        println("DBG $result")

        //assertTrue { result is JsType.IntNum }
        //assertEquals(expected = 3, actual = result.intValue())
    }
}
