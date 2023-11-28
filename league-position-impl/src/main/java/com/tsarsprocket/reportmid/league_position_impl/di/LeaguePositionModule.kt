package com.tsarsprocket.reportmid.league_position_impl.di

import com.tsarsprocket.reportmid.base.common.Mapper
import com.tsarsprocket.reportmid.base.di.PerApi
import com.tsarsprocket.reportmid.league_position_api.data.LeaguePositionRepository
import com.tsarsprocket.reportmid.league_position_api.model.LeaguePosition
import com.tsarsprocket.reportmid.league_position_impl.data.LeaguePositionMapper
import com.tsarsprocket.reportmid.league_position_impl.data.LeaguePositionRepositoryImpl
import com.tsarsprocket.reportmid.league_position_impl.retrofit.LeaguePositionDto
import com.tsarsprocket.reportmid.lol_services_api.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.lol_services_api.riotapi.getService
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface LeaguePositionModule {

    @Binds
    @PerApi
    fun bind(mapper: LeaguePositionMapper): Mapper<LeaguePositionDto, LeaguePosition>

    @Binds
    @PerApi
    fun bindLeaguePositionRepository(repository: LeaguePositionRepositoryImpl): LeaguePositionRepository

    companion object {

        @Provides
        @PerApi
        fun provideLeagueV4ServiceProvider(serviceFactory: ServiceFactory): LeagueV4ServiceProvider {
            return LeagueV4ServiceProvider { region -> serviceFactory.getService(region) }
        }
    }
}