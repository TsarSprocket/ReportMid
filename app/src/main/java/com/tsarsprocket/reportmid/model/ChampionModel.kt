package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import com.merakianalytics.orianna.types.core.staticdata.Champion
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

const val RES_NAME_PREFIX_CHAMPION = "champion_"

data class ChampionModel(
    val repository: Repository,
    val shadowChampion: Champion
) {
    val id = shadowChampion.id
    val name: String = shadowChampion.name
    val icon: Single<Drawable> by lazy { loadBitmap() }

    private fun loadBitmap(): Single<Drawable> = repository.iconProvider.getChampionIcon(shadowChampion.key)
}