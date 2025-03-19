package com.tsarsprocket.reportmid.navigationMapImpl.di

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.findSummonerApi.navigation.FindSummonerNavigation
import com.tsarsprocket.reportmid.landingApi.viewIntent.LandingIntent
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.viewStateApi.di.ViewIntentKey
import com.tsarsprocket.reportmid.viewStateApi.navigation.Navigation
import com.tsarsprocket.reportmid.viewStateApi.navigation.ReturnProcessor
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.postReturnIntent
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Module
internal class FindSummonerNavigationModule {

    // Processor declaration for Find Summoner result mapping
    interface FindSummonerResultProcessor {
        fun getCancelIntent(): ViewIntent
        fun getSuccessIntent(puuid: String, region: Region): ViewIntent
    }

    // General rule for return processing for Find Summoner capability
    @Provides
    @PerApi
    @Navigation(FindSummonerNavigation.TAG)
    fun provideFindSummonerNavigation(
        @ReturnProcessor(FindSummonerResultProcessor::class)
        resultProcessors: Map<Class<out ViewIntent>, @JvmSuppressWildcards Provider<FindSummonerResultProcessor>>,
    ) = object : FindSummonerNavigation {

        override fun ViewStateHolder.returnCancel() = postReturnIntent(resultProcessors) { getCancelIntent() }

        override fun ViewStateHolder.returnSuccess(puuid: String, region: Region) = postReturnIntent(resultProcessors) { getSuccessIntent(puuid, region) }
    }

    // Individual return mapping
    @Provides
    @ReturnProcessor(FindSummonerResultProcessor::class)
    @PerApi
    @IntoMap
    @ViewIntentKey(LandingIntent::class)
    fun provideLandingProcessor() = object : FindSummonerResultProcessor {

        override fun getCancelIntent() = LandingIntent.QuitViewIntent

        override fun getSuccessIntent(puuid: String, region: Region): ViewIntent = LandingIntent.SummonerFoundViewIntent(Puuid(puuid), region)
    }
}
