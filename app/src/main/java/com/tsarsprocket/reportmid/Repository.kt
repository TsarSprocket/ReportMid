package com.tsarsprocket.reportmid

import android.content.Context
import com.google.common.io.CharSource
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.Region
import com.merakianalytics.orianna.types.core.summoner.Summoner
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor( private val context: Context ) {

    init {

        Orianna.loadConfiguration( CharSource.wrap( loadOriannaConfigToString() ) )
    }

    private fun loadOriannaConfigToString() = InputStreamReader( context.resources.openRawResource( R.raw.orianna_config ) ).readText()

    fun summonerForName( summonerName: String, region: Region ): Summoner {

        return Summoner.named( summonerName ).withRegion( region ).get()
    }
}