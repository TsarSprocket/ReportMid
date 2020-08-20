package com.tsarsprocket.reportmid.model

import android.graphics.Bitmap
import com.merakianalytics.orianna.types.core.spectator.GameCustomizationObject
import com.merakianalytics.orianna.types.core.spectator.Player
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class PlayerModel( val repository: Repository, private val shadowPlayer: Player ) {
    val champion by lazy { repository.getChampionModel{ shadowPlayer.champion } }
    val summoner by lazy { repository.getSummonerModel{ shadowPlayer.summoner } }
    val profileIcon by lazy { BehaviorSubject.create<Bitmap>().also { subject ->
        Observable.fromCallable{ shadowPlayer.profileIcon.image.get() }.observeOn( Schedulers.io() ).subscribe( subject ) }
    }
    val summonerSpellD by lazy { repository.getSummonerSpell { shadowPlayer.summonerSpellD } }
    val summonerSpellF by lazy { repository.getSummonerSpell { shadowPlayer.summonerSpellF } }
    val isBot = shadowPlayer.isBot
    val runes by lazy { repository.getPlayerRunes { shadowPlayer.runes } }
    val primaryRunePath by lazy { runes.blockingSingle().primaryPath }
    val secondaryRunePath by lazy { runes.blockingSingle().secondaryPath }
}
