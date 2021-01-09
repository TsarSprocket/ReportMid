package com.tsarsprocket.reportmid.model

import android.graphics.drawable.Drawable
import com.tsarsprocket.reportmid.RIOTIconProvider
import io.reactivex.Single

class ChampionModel(
    val id: Int,
    val strId: String,
    val name: String,
    iconName: String,
    iconProvider: RIOTIconProvider,
) {
    val icon: Single<Drawable> by lazy { iconProvider.getChampionIcon(iconName) }
}