package com.tsarsprocket.reportmid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.MatchHistoryModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.model.SummonerModel
import com.tsarsprocket.reportmid.presentation.MatchResultPreviewData
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MatchHistoryViewModel @Inject constructor( private val repository: Repository ) : ViewModel() {

    val activeSummonerModel = MutableLiveData<SummonerModel>()

    val allDisposables = CompositeDisposable()

    fun initialize( puuid: String ) {
        allDisposables.add( repository.findSummonerByPuuid( puuid ).subscribe { activeSummonerModel.postValue( it ) } )
    }

    fun fetchMatchPreviewInfo(
        position: Int
    ): Observable<MatchResultPreviewData> {
        return Observable.create<SummonerModel> { emitter ->
                activeSummonerModel.observeForever { summonerModel ->
                    emitter.onNext( summonerModel )
                    emitter.onComplete()
                } }
            .flatMap { summonerModel ->
                summonerModel.matchHistory
                    .flatMap { it.getMatch( position) }
                    .map { matchModel -> Pair( summonerModel, matchModel ) }
            }
            .observeOn( Schedulers.io() )
            .map { pair ->
                val asParticipant = Repository.findParticipant( pair.second, pair.first )
                val asChampion = asParticipant.champion.blockingSingle()

                val runeModels = asParticipant.runeStats.blockingSingle().map { o -> o.blockingSingle().rune.blockingSingle() }

                val maybeSecondaryPath = asParticipant.secondaryRunePath.blockingSingle()

                MatchResultPreviewData(
                    asChampion.bitmap.blockingSingle(),
                    asParticipant.kills,
                    asParticipant.deaths,
                    asParticipant.assists,
                    asParticipant.isWinner,
                    pair.second.remake,
                    asParticipant.creepScore,
                    pair.second.gameType.titleResId,
                    runeModels.find { it.slot == 0 }?.iconResId,
                    if( maybeSecondaryPath.isEmpty.blockingGet() ) null else maybeSecondaryPath.blockingGet().iconResId,
                    asParticipant.summonerSpellD.blockingSingle().icon.blockingSingle(),
                    asParticipant.summonerSpellF.blockingSingle().icon.blockingSingle(),
                    Repository.getItemIcons( asParticipant )
                )
            }
    }

    override fun onCleared() {
        allDisposables.clear()
        super.onCleared()
    }
}