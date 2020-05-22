package com.tsarsprocket.reportmid

import android.content.Context
import com.google.common.io.CharSource
import com.merakianalytics.orianna.Orianna
import com.tsarsprocket.reportmid.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor( val context: Context ) {

    init {

        Orianna.loadConfiguration( CharSource.wrap( loadOriannaConfigToString() ) )
    }

    private fun loadOriannaConfigToString(): String {

        context.resources.openRawResource( R.raw.orianna_config )
    }
}