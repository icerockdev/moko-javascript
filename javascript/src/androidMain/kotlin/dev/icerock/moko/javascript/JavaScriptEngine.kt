/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import app.cash.quickjs.QuickJs
import app.cash.quickjs.QuickJsException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

actual class JavaScriptEngine actual constructor() {
    private val quickJs: QuickJs = QuickJs.create()
    private val json: Json = Json.Default

    private var contextObjects: Map<String, JsType> = emptyMap()

    @Volatile
    var isClosed = false
        private set

    actual fun setContextObjects(vararg context: Pair<String, JsType>) {
        this.contextObjects = mapOf(*context)
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
        quickJs.close()
        isClosed = true
    }

    private fun internalEvaluate(
        context: Map<String, JsType>,
        script: String
    ): JsType {
        val scriptContext: Map<String, JsType> = contextObjects + context
        val jsContext: ContextProvider = ContextProviderImpl(
            context = scriptContext,
            script = script
        )
        quickJs.set("mokoJsContext", ContextProvider::class.java, jsContext)

        val scriptWithContext: String = buildString {
            scriptContext.forEach { (name, jsType) ->
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
            append("const script = mokoJsContext.getScript();\n")
            append("const result = eval(script);\n")
            append("if (typeof result === 'object') JSON.stringify(result);\n")
            append("else if (typeof result === 'array') JSON.stringify(result);\n")
            append("else result;")
        }
        val result: Any? = quickJs.evaluate(scriptWithContext)
        return handleQuickJsResult(result)
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
            } catch (ex: SerializationException) {
                JsType.Str(result)
            } catch (ex: IllegalStateException) {
                JsType.Str(result)
            }
            else -> throw JavaScriptEvaluationException(
                message = "Impossible JavaScriptEngine handler state with result [$result]"
            )
        }
    }
}

private interface ContextProvider {
    fun getBool(name: String): Boolean
    fun getDouble(name: String): Double

    fun getString(name: String): String

    fun getScript(): String
}

private class ContextProviderImpl(
    private val context: Map<String, JsType>,
    private val script: String
) : ContextProvider {
    override fun getBool(name: String): Boolean {
        return context[name]!!.boolValue()
    }

    override fun getDouble(name: String): Double {
        return context[name]!!.doubleValue()
    }

    override fun getString(name: String): String {
        val jsType: JsType = context[name]!!
        return when (jsType) {
            is JsType.Bool, is JsType.DoubleNum, JsType.Null -> throw IllegalArgumentException()
            is JsType.Json -> jsType.value.toString()
            is JsType.Str -> jsType.value
        }
    }

    override fun getScript(): String {
        return this.script
    }
}
