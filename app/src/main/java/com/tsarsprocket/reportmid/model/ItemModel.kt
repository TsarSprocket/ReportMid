package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.RIOTIconProvider

class ItemModel(
    val riotId: Int,
    val name: String,
    imageName: String,
    iconProvider: RIOTIconProvider,
) {
    val icon by lazy { iconProvider.getItemIcon(imageName).cache() }
}