package dev.icerock.moko.javascript

class JavaScriptEvaluationException(
    throwable: Throwable?,
    message: String? = null
) : Exception(message, throwable)
