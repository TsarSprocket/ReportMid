package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.match.RuneStats

class RuneStatsModel( val repository: Repository, private val shadowRuneStats: RuneStats ) {
    val rune by lazy { repository.getRune(shadowRuneStats.rune.id) }
}