package com.tsarsprocket.reportmid.lol.api.model

interface ConstrainableString<T> {
    val value: String

    val minLength: Int
    val maxLength: Int

    val isValid: Boolean

    fun copyWithNewValue(newValue: String): T
}

fun <T : ConstrainableString<T>> T.removeWhitespaces(): T = copyWithNewValue(value.filterNot { it.isWhitespace() })
