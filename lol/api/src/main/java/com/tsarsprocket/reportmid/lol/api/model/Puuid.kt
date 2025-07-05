package com.tsarsprocket.reportmid.lol.api.model

import com.tsarsprocket.reportmid.utils.common.EMPTY_STRING

@JvmInline
value class Puuid(val value: String) {

    companion object {
        val EMPTY = Puuid(EMPTY_STRING)
    }
}