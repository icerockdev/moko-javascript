/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import app.cash.quickjs.QuickJs
import app.cash.quickjs.QuickJsException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

actual class JavaScriptEngine actual constructor(){
    private val quickJs: QuickJs = QuickJs.create()
    private val json: Json = Json.Default

    @Volatile
    var isClosed = false
        private set

    actual fun evaluate(
        context: Map<String, JsType>,
        script: String
    ): JsType {
        if (isClosed) throw JavaScriptEvaluationException(message = "Engine already closed")

        return try {
            internalEvaluate(context, script)
        } catch (exception: QuickJsException) {
            throw JavaScriptEvaluationException(exception)
        }
    }

    actual fun close() {
        if (isClosed) return
        quickJs.close()
        isClosed = true
    }

    private fun internalEvaluate(
        context: Map<String, JsType>,
        script: String
    ): JsType {
        context.forEach { pair ->
            quickJs.set(pair.key, pair.value.javaClass, pair.value)
        }
        val result = quickJs.evaluate(script)
        return handleQuickJsResult(result)
    }

    private fun handleQuickJsResult(result: Any?): JsType {
        return when (result) {
            result == null -> JsType.Null
            is Boolean -> JsType.Bool(result)
            is Int -> JsType.IntNum(result)
            is Double -> JsType.DoubleNum(result)
            is Float -> JsType.DoubleNum(result.toDouble())
            is String -> try {
                JsType.Json(json.encodeToJsonElement(result))
            } catch (ex: SerializationException) {
                JsType.Str(result)
            }
            else -> throw JavaScriptEvaluationException(
                message = "Impossible JavaScriptEngine handler state with result [$result]"
            )
        }
    }
}
