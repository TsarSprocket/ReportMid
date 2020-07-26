package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.merakianalytics.orianna.types.core.staticdata.Item
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableFromCallable
import io.reactivex.schedulers.Schedulers

const val RES_NAME_PREFIX_ITEM = "item_"

class ItemModel( val repository: Repository, private val shadowItem: Item ) {
    val bitmap by lazy { loadBitmap().replay( 1 ).autoConnect() }

    private fun loadBitmap(): Observable<Bitmap> = ObservableFromCallable {
            val resId = repository.context.resources.getIdentifier( RES_NAME_PREFIX_ITEM + shadowItem.id,"drawable", repository.context.packageName )
            BitmapFactory.decodeResource( repository.context.resources, resId )!!
        }.subscribeOn( Schedulers.io() )
}