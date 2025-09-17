package com.tsarsprocket.reportmid.lol.api.model

import com.tsarsprocket.reportmid.utils.common.noWhitespaces

const val MAX_GAME_NAME_LENGTH = 16
const val MAX_TAG_LINE_LENGTH = 5
const val MIN_GAME_NAME_LENGTH = 3
const val MIN_TAG_LINE_LENGTH = 3


fun isGameNameValid(gameName: String): Boolean = gameName.noWhitespaces.let { whiteSpaceSkipped ->
    whiteSpaceSkipped.length in MIN_GAME_NAME_LENGTH..MAX_GAME_NAME_LENGTH && whiteSpaceSkipped.all { char -> char.isLetterOrDigit() }
}

fun isTagLineValid(tagLine: String): Boolean = tagLine.noWhitespaces.let { whiteSpaceSkipped ->
    whiteSpaceSkipped.length in MIN_TAG_LINE_LENGTH..MAX_TAG_LINE_LENGTH && whiteSpaceSkipped.all { it.isLetterOrDigit() }
}