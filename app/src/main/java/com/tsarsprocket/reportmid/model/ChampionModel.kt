package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.merakianalytics.orianna.types.core.staticdata.Champion
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

data class ChampionModel(
    val repository: Repository,
    val shadowChampion: Champion
) {
    val id = shadowChampion.id
    val bitmap: Observable<Bitmap> by lazy { loadBitmap().replay( 1 ).autoConnect() }
    val name: String = shadowChampion.name

    private fun loadBitmap(): Observable<Bitmap> {
        return Observable.fromCallable() {
            val resId = repository.context.resources.getIdentifier( shadowChampion.key.toLowerCase(),"drawable", repository.context.packageName )
            BitmapFactory.decodeResource( repository.context.resources, resId )
        }.subscribeOn( Schedulers.io() )
    }
}