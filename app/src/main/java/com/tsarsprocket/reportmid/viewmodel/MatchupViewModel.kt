package com.tsarsprocket.reportmid.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.*
import com.tsarsprocket.reportmid.presentation.PlayerPresentation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.logging.LogManager
import javax.inject.Inject

const val STR_NO_DURATION = "--:--"

private const val MILLIS_IN_SECOND = 1000
private const val SECONDS_IN_MINUTE = 60
private const val MILLIS_IN_MINUTE = MILLIS_IN_SECOND * SECONDS_IN_MINUTE
private const val MINUTES_IN_HOUR = 60
private const val MILLIS_IN_HOUR = MILLIS_IN_MINUTE * MINUTES_IN_HOUR

private val DUR_FMT_HMS = "%02d:%02d:%02d"
private val DUR_FMT_MS = "%02d:%02d"

class MatchupViewModel @Inject constructor( private val repository: Repository ): ViewModel() {

    lateinit var puuid: String

    var summoner = MutableLiveData<SummonerModel>()

    val matchInProgress = MutableLiveData<Boolean>()

    val currentMatchLive = MutableLiveData<CurrentMatchModel>()

    val gameTypeTextLive = Transformations.map( currentMatchLive ) { match -> match.gameType.name }
// Temporary removed:
//    val durationTextLive = MutableLiveData<String>( STR_NO_DURATION )

    val blueTeamParticipants = MutableLiveData<List<PlayerPresentation>>()
    val redTeamParticipants = MutableLiveData<List<PlayerPresentation>>()

    val allDisposables = CompositeDisposable()
    val matchDisposables = CompositeDisposable()

    init {
        allDisposables.add( Observable.interval( 1, TimeUnit.SECONDS ).observeOn( AndroidSchedulers.mainThread() ).subscribe{
            updateDuration()
        } )
    }

    fun loadForSummoner( puuid: String ) {
        allDisposables.add( repository.findSummonerByPuuid( puuid )
            .observeOn( AndroidSchedulers.mainThread() )
            .flatMap{ summoner.value = it; it.getCurrentMatch() }
            .observeOn( AndroidSchedulers.mainThread() )
            .subscribe( { match ->
                currentMatchLive.value = match
                matchDisposables.clear()
                obtainTeamParticipants( match.blueTeam, blueTeamParticipants, matchDisposables )
                obtainTeamParticipants( match.redTeam, redTeamParticipants, matchDisposables )
                matchInProgress.value = true
            } ) {
                matchDisposables.clear()
                matchInProgress.value = false
            } )
    }

    private fun obtainTeamParticipants(
        obsCurTeamModel: ReplaySubject<CurrentMatchTeamModel>,
        participantsListLive: MutableLiveData<List<PlayerPresentation>>,
        disposer: CompositeDisposable
    ) {
        disposer.add(
            obsCurTeamModel
                .flatMap { matchTeamModel -> matchTeamModel.participants }
                .observeOn( Schedulers.io() )
                .subscribe { lstObsPlayerModels ->
                    participantsListLive.postValue(
                        lstObsPlayerModels
                            .map { subject -> subject.blockingSingle() }
                            .map { playerModel ->
                                val playerPresentation =  PlayerPresentation()
                                matchDisposables.addAll(
                                    playerModel.champion.flatMap { it.bitmap }.subscribe{ playerPresentation.championIconLive.postValue( it ) },
                                    Observable.zip( playerModel.champion, playerModel.summoner, BiFunction<ChampionModel,SummonerModel,Observable<Int>> { ch, su -> getSkillForChampion( su, ch ) } )
                                        .flatMap { it }
                                        .concatWith( Observable.just( -1 ) )
                                        .take( 1 )
                                        .subscribe { playerPresentation.summonerChampionSkillLive.postValue( it ) },
                                    playerModel.summoner.subscribe { playerPresentation.summonerNameLive.postValue( it.name ); playerPresentation.summonerLevelLive.postValue( it.level ) },
                                    playerModel.summoner.flatMap { it.soloQueuePosition }.subscribe { soloQueuePosition ->
                                        playerPresentation.soloqueueRankLive.postValue( soloQueuePosition.tier.shortName + soloQueuePosition.division.numeric )
                                        playerPresentation.soloqueueWinrateLive.postValue( ( soloQueuePosition.wins.toFloat() / ( soloQueuePosition.wins + soloQueuePosition.losses ).toFloat() ).takeUnless { it.isNaN() }?: 0f )
                                    },
                                    playerModel.summonerSpellD.flatMap { it.icon }.subscribe{ playerPresentation.summonerSpellDLive.postValue( it ) },
                                    playerModel.summonerSpellF.flatMap { it.icon }.subscribe{ playerPresentation.summonerSpellFLive.postValue( it ) }
                                )
                                playerPresentation.primaryRunePathIconResIdLive.postValue( playerModel.primaryRunePath.iconResId )
                                playerPresentation.secondaryRunePathIconResIdLive.postValue( playerModel.secondaryRunePath.iconResId )

                                playerPresentation
                            }
                    )
                }
        )
    }

    private fun getSkillForChampion( summoner: SummonerModel, champion: ChampionModel ) =
        summoner.getMasteryWithChampion( champion ).map { it.points }

    @MainThread
    private fun updateDuration() {
        val currentMatch = currentMatchLive.value
        if( currentMatch != null ) {
            val durMillis = GregorianCalendar.getInstance().timeInMillis - currentMatch.creationMoment.timeInMillis
            val hours = durMillis / MILLIS_IN_HOUR
            val minutes = durMillis / MILLIS_IN_MINUTE - hours * MINUTES_IN_HOUR
            val seconds = durMillis / MILLIS_IN_SECOND - ( hours * MINUTES_IN_HOUR + minutes ) * SECONDS_IN_MINUTE
// Temporary removed:
//            durationTextLive.value = if( hours > 0 ) Formatter().format( DUR_FMT_HMS, hours, minutes, seconds ).toString() else Formatter().format( DUR_FMT_MS, minutes, seconds ).toString()
        }
    }

    override fun onCleared() {
        allDisposables.clear()
        super.onCleared()
    }
}