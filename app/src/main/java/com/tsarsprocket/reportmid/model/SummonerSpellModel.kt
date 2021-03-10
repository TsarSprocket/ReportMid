package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.staticdata.SummonerSpell
import com.tsarsprocket.reportmid.RIOTIconProvider
import com.tsarsprocket.reportmid.room.ddragon.SummonerSpellEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class SummonerSpellModel(
    val key: Long,
    iconName: String,
    iconProvider: RIOTIconProvider,
) {
    val icon by lazy { iconProvider.getSummonerSpellIcon(iconName).cache() }
}