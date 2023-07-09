package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.RIOTIconProvider

class SummonerSpellModel(
    val key: Long,
    iconName: String,
    iconProvider: RIOTIconProvider,
) {
    val icon by lazy { iconProvider.getSummonerSpellIcon(iconName).cache() }
}