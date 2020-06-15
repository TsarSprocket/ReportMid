package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import com.merakianalytics.orianna.types.core.staticdata.Champion

data class ChampionModel(val shadowChampion: Champion ) {

    lateinit var bitmap: Bitmap

    fun load() {
        bitmap = shadowChampion.image.get()
    }
}