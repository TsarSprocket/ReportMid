package com.tsarsprocket.reportmid.lol.model

@JvmInline
value class TagLine(override val value: String) : ConstrainableString<TagLine> {
    override val minLength: Int
        get() = 3
    override val maxLength: Int
        get() = 5

    override val isValid: Boolean
        get() = value.length in minLength..maxLength && value.all { it.isLetterOrDigit() || it.isWhitespace() }

    override fun copyWithNewValue(newValue: String): TagLine = TagLine(newValue)
}