/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import app.cash.quickjs.QuickJs
import app.cash.quickjs.QuickJsException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

actual class JavaScriptEngine actual constructor() {
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
            throw JavaScriptEvaluationException(exception, exception.message)
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
        val scriptWithContext = convertContextMapToJsScript(context) + script
        val result = quickJs.evaluate(scriptWithContext)
        return handleQuickJsResult(result)
    }

    private fun convertContextMapToJsScript(context: Map<String, JsType>): String {
        if (context.isEmpty()) return ""

        return context.mapNotNull { pair ->
            pair.value.value?.let { "var ${pair.key} = $it;" }
        }.joinToString(separator = "")
    }

    private fun handleQuickJsResult(result: Any?): JsType {
        return when {
            result == null -> JsType.Null
            result is Boolean -> JsType.Bool(result)
            result is Int -> JsType.IntNum(result)
            result is Double -> JsType.DoubleNum(result)
            result is Float -> JsType.DoubleNum(result.toDouble())
            result is String -> try {
                val serializeResult = json.parseToJsonElement(result)
                if (serializeResult is JsonObject) {
                    JsType.Json(serializeResult)
                } else {
                    JsType.Str(result)
                }
            } catch (ex: SerializationException) {
                JsType.Str(result)
            }
            else -> throw JavaScriptEvaluationException(
                message = "Impossible JavaScriptEngine handler state with result [$result]"
            )
        }
    }
}
