package com.tsarsprocket.reportmid

import android.content.Context
import com.merakianalytics.orianna.Orianna
import com.tsarsprocket.reportmid.model.Region
import com.tsarsprocket.reportmid.model.Summoner
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor( private val context: Context ) {

    init {
//        Orianna.loadConfiguration( CharSource.wrap( loadOriannaConfigToString() ) )
        Orianna.setRiotAPIKey( "RGAPI-c7fb4b5e-3f60-4d6b-99a3-02066fee6da3" )
        Orianna.setDefaultRegion( com.merakianalytics.orianna.types.common.Region.RUSSIA )
    }

    private fun loadOriannaConfigToString() = InputStreamReader( context.resources.openRawResource( R.raw.orianna_config ) ).readText()

    val allRegions = Region.values()

    fun summonerForName( summonerName: String, region: Region ): Summoner {

        val sum = Orianna.summonerNamed(summonerName).withRegion(region.shadowRegion).get()

        return Summoner(
            sum.name,
            sum.profileIcon.image.get(),
            sum )
    }
}