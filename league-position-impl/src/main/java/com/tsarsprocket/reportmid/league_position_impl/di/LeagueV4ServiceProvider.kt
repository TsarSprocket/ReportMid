package com.tsarsprocket.reportmid.league_position_impl.di

import com.tsarsprocket.reportmid.league_position_impl.retrofit.LeagueV4Service
import com.tsarsprocket.reportmid.lol.model.Region

fun interface LeagueV4ServiceProvider {
    operator fun invoke(region: Region): LeagueV4Service
}