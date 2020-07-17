package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.staticdata.SummonerSpell
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class SummonerSpellModel( val repository: Repository, val shadowSummonerSpell: SummonerSpell ) {
    val icon by lazy { Observable.fromCallable { shadowSummonerSpell.image.get() }.subscribeOn( Schedulers.io() ).replay( 1 ).autoConnect() }
}