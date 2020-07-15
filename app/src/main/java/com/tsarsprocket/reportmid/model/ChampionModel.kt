package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.merakianalytics.orianna.types.core.staticdata.Champion
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

const val RES_NAME_PREFIX_CHAMPION = "champion_"

data class ChampionModel(
    val repository: Repository,
    val shadowChampion: Champion
) {
    val id = shadowChampion.id
    val bitmap: Observable<Bitmap> by lazy { loadBitmap().replay( 1 ).autoConnect() }
    val name: String = shadowChampion.name

    private fun loadBitmap(): Observable<Bitmap> =
        Observable.fromCallable() {
            val resId = repository.context.resources.getIdentifier( RES_NAME_PREFIX_CHAMPION + shadowChampion.key.toLowerCase(),"drawable", repository.context.packageName )
            BitmapFactory.decodeResource( repository.context.resources, resId )
        }.subscribeOn( Schedulers.io() )
}