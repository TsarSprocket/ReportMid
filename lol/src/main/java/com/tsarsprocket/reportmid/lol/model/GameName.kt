package com.tsarsprocket.reportmid.lol.model

@JvmInline
value class GameName(override val value: String) : ConstrainableString<GameName> {
    override val minLength
        get() = 3
    override val maxLength: Int
        get() = 16

    override val isValid: Boolean
        get() = value.length in minLength..maxLength && value.all { it.isLetterOrDigit() || it.isWhitespace() }

    override fun copyWithNewValue(newValue: String): GameName = GameName(newValue)
}
