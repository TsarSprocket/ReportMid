package com.tsarsprocket.reportmid.matchData.api.data.model

class MatchNotFoundException : Exception(MESSAGE) {

    companion object {
        const val MESSAGE = "Match not found"
    }
}