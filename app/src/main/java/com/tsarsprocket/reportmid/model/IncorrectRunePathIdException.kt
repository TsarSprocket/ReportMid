package com.tsarsprocket.reportmid.model

import java.util.Formatter

private const val INCORRECT_RUNE_PATH_ID_MESSAGE = "Incorrect rune path ID: %d"

class IncorrectRunePathIdException( pathId: Int ) : RuntimeException( Formatter().format( INCORRECT_RUNE_PATH_ID_MESSAGE, pathId ).toString() )