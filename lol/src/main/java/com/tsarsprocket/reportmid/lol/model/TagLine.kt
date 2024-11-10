package com.tsarsprocket.reportmid.lol.model

@JvmInline
value class TagLine(val value: String) {

    val isValid: Boolean
        get() = value.length in GameName.MIN_LENGTH..GameName.MAX_LENGTH && value.all { it.isLetterOrDigit() || it.isWhitespace() }

    fun removeWhitespaces() = GameName(value.filterNot { it.isWhitespace() })

    companion object {
        const val MIN_LENGTH = 3
        const val MAX_LENGTH = 5
    }
}