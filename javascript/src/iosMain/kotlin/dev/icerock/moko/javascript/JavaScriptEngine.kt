/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import platform.Foundation.NSArray
import platform.Foundation.NSDictionary
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSNull
import platform.Foundation.NSNumber
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.JavaScriptCore.JSContext
import platform.JavaScriptCore.JSValue
import platform.JavaScriptCore.setObject

actual class JavaScriptEngine actual constructor() {

    private val jsContext = JSContext().apply {
        exceptionHandler = { exceptionContext, exception ->
            val message = "\"context = $exceptionContext, exception = $exception\""
            throw JavaScriptEvaluationException(cause = null, message = message)
        }
    }

    actual fun setContextObjects(vararg context: Pair<String, Any>) {
        context.forEach { (key, value) ->
            val contextObject = if (value is JsType) {
                prepareValueForJsContext(value)
            } else {
                value
            }
            jsContext.setObject(
                `object` = contextObject,
                forKeyedSubscript = NSString.create(string = key)
            )
        }
    }

    actual fun evaluate(context: Map<String, JsType>, script: String): JsType {
        context.forEach { (key, value) ->
            jsContext.setObject(
                `object` = prepareValueForJsContext(value),
                forKeyedSubscript = NSString.create(string = key)
            )
        }

        val result = jsContext.evaluateScript(script)

        context.forEach { (key, _) ->
            jsContext.setObject(
                `object` = null,
                forKeyedSubscript = NSString.create(string = key)
            )
        }

        return result?.toMokoJSType() ?: JsType.Null
    }

    actual fun close() {
        // Nothing to do here
    }

//    actual fun objectToJsonString(value: JsType): String? {
//        val localContext = JSContext()
//        localContext.setObject(
//            `object` = prepareValueForJsContext(value),
//            forKeyedSubscript = NSString.create(string = "objectValue")
//        )
//
//        return localContext.evaluateScript("JSON.stringify(objectValue);")?.toString_()
//    }

    private fun prepareValueForJsContext(valueWrapper: JsType): Any? {
        return if (valueWrapper is JsType.Json) valueWrapper.value.getValue()
        else valueWrapper.value
    }
}

private fun JsonObject.toNSDictionary(): NSDictionary {
    val data = NSString.create(string = this.toString()).dataUsingEncoding(NSUTF8StringEncoding)
        ?: return NSDictionary()
    return (NSJSONSerialization.JSONObjectWithData(
        data = data,
        options = 0,
        error = null
    ) as? NSDictionary) ?: NSDictionary()
}

private fun JsonArray.toNSArray(): NSArray {
    val data = NSString.create(string = this.toString()).dataUsingEncoding(NSUTF8StringEncoding)
        ?: return NSArray()
    return (NSJSONSerialization.JSONObjectWithData(
        data = data,
        options = 0,
        error = null
    ) as? NSArray) ?: NSArray()
}

private fun JsonElement.getValue(): Any? {
    return (this as? JsonObject)?.toNSDictionary()
        ?: (this as? JsonArray)?.toNSArray()
        ?: (this as? JsonPrimitive)?.content
}

private fun JSValue.toMokoJSType(): JsType {
    return when {
        isBoolean -> JsType.Bool(toBool())
        isString -> JsType.Str(toString_().orEmpty())
        isNumber -> JsType.DoubleNum(toDouble())
        isObject -> this.toDictionary()!!
            .toJson()
            .let { JsType.Json(it) }
        isArray -> this.toArray()!!
            .map { it.toJsonElement() }
            .let { JsType.Json(JsonArray(it)) }
        isNull || isUndefined -> JsType.Null
        else -> throw IllegalArgumentException("unknown JSValue type $this")
    }
}

private fun Map<Any?, *>.toJson(): JsonObject {
    return this.mapKeys { it.key as String
    }.mapValues { it.value.toJsonElement() }
        .let { JsonObject(it) }
}

/**
 * @see https://developer.apple.com/documentation/javascriptcore/jsvalue?language=objc#1663421
 */
private fun Any?.toJsonElement(): JsonElement {
    return when(this) {
        null -> JsonNull
        is NSNull -> JsonNull
        is NSString -> JsonPrimitive(this as String)
        is NSNumber -> JsonPrimitive(this.doubleValue)
        is NSDictionary -> (this as Map<Any?, *>).toJson()
        is NSArray -> (this as List<*>).map { it.toJsonElement() }.let { JsonArray(it) }
        else -> throw IllegalArgumentException("unknown JSValue type $this")
    }
}
