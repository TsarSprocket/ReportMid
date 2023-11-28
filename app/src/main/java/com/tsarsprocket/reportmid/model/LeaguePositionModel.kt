package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.league.LeagueEntry

class LeaguePositionModel(shadowLeagueEntry: LeagueEntry) {
    val tier = Repository.getTier(shadowLeagueEntry.tier)
    val division = Repository.getDivision(shadowLeagueEntry.division)
    val wins = shadowLeagueEntry.wins
    val losses = shadowLeagueEntry.losses
}