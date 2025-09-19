/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import app.cash.zipline.EngineApi
import app.cash.zipline.QuickJsException
import app.cash.zipline.Zipline
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@OptIn(EngineApi::class)
actual class JavaScriptEngine {

    private val json: Json = Json
    private val jsContext: ContextProviderDynamic = ContextProviderDynamic()
    private val zipline: Zipline = Zipline.create(dispatcher = Dispatchers.IO)

    @Volatile
    var isClosed = false
        private set

    init {
        zipline.bind<ContextProvider>("mokoJsContext", jsContext)
        zipline.quickJs.evaluate(
            """
                function mokoJavaScriptProcessResult(result) {
                    if (typeof result === 'object') return JSON.stringify(result);
                    else if (typeof result === 'array') return JSON.stringify(result);
                    else return result;
                }
            """.trimIndent()
        )
    }

    actual fun setContextObjects(vararg context: Pair<String, JsType>) {
        val scriptContext: Map<String, JsType> = context.toMap()
        jsContext.context = scriptContext

        val scriptWithContext: String = buildString {
            fillContext(scriptContext)
        }
        zipline.quickJs.evaluate(scriptWithContext)
    }

    actual fun evaluate(context: Map<String, JsType>, script: String): JsType {
        if (isClosed) throw JavaScriptEvaluationException(message = "Engine already closed")

        return try {
            internalEvaluate(context, script)
        } catch (exception: QuickJsException) {
            throw JavaScriptEvaluationException(exception, exception.message)
        }
    }

    actual fun close() {
        if (isClosed) return
        zipline.quickJs.close()
        isClosed = true
    }

    private fun internalEvaluate(
        context: Map<String, JsType>,
        script: String
    ): JsType {
        jsContext.activeScript = script
        jsContext.context = context

        val scriptWithContext: String = buildString {
            fillContext(context)
            append("mokoJavaScriptProcessResult(eval(mokoJsContext.getScript()));")
        }
        val result: Any? = zipline.quickJs.evaluate(scriptWithContext)
        return handleQuickJsResult(result)
    }

    private fun StringBuilder.fillContext(context: Map<String, JsType>) {
        context.forEach { (name, jsType) ->
            append("const ")
            append(name)
            append(" = ")
            append(
                when (jsType) {
                    is JsType.Bool -> "mokoJsContext.getBool('$name')"
                    is JsType.DoubleNum -> "mokoJsContext.getDouble('$name')"
                    is JsType.Json -> "JSON.parse(mokoJsContext.getString('$name'))"
                    JsType.Null -> "null"
                    is JsType.Str -> "mokoJsContext.getString('$name')"
                }
            )
            append(";\n")
        }
    }

    private fun handleQuickJsResult(result: Any?): JsType {
        return when (result) {
            null -> JsType.Null
            is Boolean -> JsType.Bool(result)
            is Int -> JsType.DoubleNum(result.toDouble())
            is Double -> JsType.DoubleNum(result)
            is Float -> JsType.DoubleNum(result.toDouble())
            is String -> try {
                val jsonElement: JsonElement = json.parseToJsonElement(result)
                if (jsonElement is JsonObject || jsonElement is JsonArray) JsType.Json(jsonElement)
                else JsType.Str(result)
            } catch (_: SerializationException) {
                JsType.Str(result)
            } catch (_: IllegalStateException) {
                JsType.Str(result)
            }
            else -> throw JavaScriptEvaluationException(
                message = "Impossible JavaScriptEngine handler state with result [$result]"
            )
        }
    }
}
