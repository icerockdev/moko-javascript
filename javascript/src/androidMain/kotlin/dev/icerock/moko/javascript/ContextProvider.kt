package dev.icerock.moko.javascript

internal interface ContextProvider {
    fun getBool(name: String): Boolean
    fun getDouble(name: String): Double

    fun getString(name: String): String

    fun getScript(): String
}
