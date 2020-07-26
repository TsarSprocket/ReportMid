package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.common.RunePath
import com.tsarsprocket.reportmid.R

enum class RunePathModel( val pathId: Int, val iconResId: Int ) {
    DOMINATION( 8100, R.drawable.runepath_domination ),
    INSPIRATION( 8300, R.drawable.runepath_inspiration ),
    PRECISION( 8000, R.drawable.runepath_precision ),
    RESOLVE( 8400, R.drawable.runepath_resolve ),
    SORCERY( 8200, R.drawable.runepath_sorcery );

    companion object {
        val byId = RunePathModel.values().map{ it.pathId to it }.toMap()
    }
}

