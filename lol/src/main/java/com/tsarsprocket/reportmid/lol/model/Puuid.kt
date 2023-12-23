package com.tsarsprocket.reportmid.lol.model

import com.tsarsprocket.reportmid.utils.common.EMPTY as EMPTY_STRING

@JvmInline
value class Puuid(val value: String) {

    companion object {
        val EMPTY = Puuid(EMPTY_STRING)
    }
}