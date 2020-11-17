package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import com.merakianalytics.orianna.types.core.staticdata.Item
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.internal.operators.observable.ObservableFromCallable
import io.reactivex.schedulers.Schedulers

const val RES_NAME_PREFIX_ITEM = "item_"

class ItemModel( val repository: Repository, private val shadowItem: Item ) {
    val icon by lazy { loadBitmap().subscribeOn(Schedulers.io()).cache() }

    private fun loadBitmap(): Single<Drawable> = repository.iconProvider.getItemImage(shadowItem.id)
}