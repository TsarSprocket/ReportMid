package com.tsarsprocket.reportmid.leaguePositionImpl.di

import com.tsarsprocket.reportmid.baseApi.common.Mapper
import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.leaguePositionApi.data.LeaguePositionRepository
import com.tsarsprocket.reportmid.leaguePositionApi.model.LeaguePosition
import com.tsarsprocket.reportmid.leaguePositionImpl.data.LeaguePositionMapper
import com.tsarsprocket.reportmid.leaguePositionImpl.data.LeaguePositionRepositoryImpl
import com.tsarsprocket.reportmid.leaguePositionImpl.retrofit.LeaguePositionDto
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.ServiceFactory
import com.tsarsprocket.reportmid.lolServicesApi.riotapi.getService
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