package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.RIOTIconProvider

class ItemModel(
    val riotId: Long,
    val name: String,
    imageName: String,
    iconProvider: RIOTIconProvider,
) {
    val icon by lazy { iconProvider.getItemIcon(imageName).cache() }
}