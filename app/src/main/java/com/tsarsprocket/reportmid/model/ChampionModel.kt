package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.merakianalytics.orianna.types.core.staticdata.Champion

data class ChampionModel(
    val repository: Repository,
    val shadowChampion: Champion
) {
    val id = shadowChampion.id
    lateinit var bitmap: Bitmap
    val name: String = shadowChampion.name

    fun load() {
        val resId = repository.context.resources.getIdentifier( shadowChampion.key.toLowerCase(), "drawable", repository.context.packageName )
        bitmap = BitmapFactory.decodeResource( repository.context.resources, resId )

        //shadowChampion.image.get()
    }
}