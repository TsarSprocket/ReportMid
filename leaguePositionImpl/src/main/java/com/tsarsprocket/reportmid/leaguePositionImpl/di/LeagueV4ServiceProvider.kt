package com.tsarsprocket.reportmid.leaguePositionImpl.di

import com.tsarsprocket.reportmid.leaguePositionImpl.retrofit.LeagueV4Service
import com.tsarsprocket.reportmid.lol.api.model.Region

fun interface LeagueV4ServiceProvider {
    operator fun invoke(region: Region): LeagueV4Service
}