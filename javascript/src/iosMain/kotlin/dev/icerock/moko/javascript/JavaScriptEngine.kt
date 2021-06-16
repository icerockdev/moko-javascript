/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.javascript

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import platform.Foundation.NSArray
import platform.Foundation.NSDictionary
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.JavaScriptCore.JSContext
import platform.JavaScriptCore.JSValue
import platform.JavaScriptCore.setObject

actual class JavaScriptEngine actual constructor() {
    actual fun evaluate(context: Map<String, JsType>, script: String): JsType {

        val jsContext = JSContext()

        jsContext.exceptionHandler = { exceptionContext, exception ->
            val message = "\"context = $exceptionContext, exception = $exception\""
            throw JavaScriptEvaluationException(cause = null, message = message)
        }

        context.forEach {
            jsContext.setObject(
                `object` = prepareValueForJsContext(it.value),
                forKeyedSubscript = NSString.create(string = it.key)
            )
        }

        val result = jsContext.evaluateScript(script)

        return result?.toMokoJSType() ?: JsType.Null
    }

    actual fun close() {
        // Nothing to do here
    }

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
        isObject -> JsType.Json(Json.encodeToJsonElement(toDictionary()))
        isArray -> JsType.Json(Json.encodeToJsonElement(toArray()))
        isUndefined -> JsType.Null
        isNull -> JsType.Null
        else -> JsType.Null
    }
}
