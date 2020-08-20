package com.tsarsprocket.reportmid

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tsarsprocket.reportmid.model.*
import com.tsarsprocket.reportmid.presentation.PlayerPresentation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit
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

    lateinit var summoner: SummonerModel

    val currentMatchLive = MutableLiveData<CurrentMatchModel>()

    val gameTypeTextLive = Transformations.map( currentMatchLive ) { match -> match.gameType.name }
    val durationTextLive = MutableLiveData<String>( STR_NO_DURATION )

    val blueTeamParticipants = MutableLiveData<List<PlayerPresentation>>()
    val redTeamParticipants = MutableLiveData<List<PlayerPresentation>>()

    val allDisposables = CompositeDisposable()
    val matchDisposables = CompositeDisposable()

    init {
        allDisposables.add( Observable.timer( 1, TimeUnit.SECONDS ).observeOn( AndroidSchedulers.mainThread() ).subscribe{
            updateDuration()
        } )
    }

    fun loadForSummoner( puuid: String ) {
        allDisposables.add( repository.findSummonerByPuuid( puuid )
            .observeOn( AndroidSchedulers.mainThread() )
            .flatMap{ summoner = it; it.currentMatch }
            .observeOn( AndroidSchedulers.mainThread() )
            .subscribe{ match ->
                currentMatchLive.value = match
                matchDisposables.clear()
                obtainTeamParticipants( match.blueTeam, blueTeamParticipants, matchDisposables )
                obtainTeamParticipants( match.redTeam, redTeamParticipants, matchDisposables )
            } )
    }

    private fun obtainTeamParticipants(
        obsCurTeamModel: BehaviorSubject<CurrentMatchTeamModel>,
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
                                val champion = playerModel.champion.blockingSingle()
                                val summoner = playerModel.summoner.blockingSingle()
                                val soloQueuePosition = summoner.soloQueuePosition.blockingSingle()
                                PlayerPresentation(
                                    champion.bitmap.blockingSingle(),
                                    getSkillForChampion( summoner, champion ),
                                    summoner.name,
                                    summoner.level,
                                    soloQueuePosition.tier.shortName + soloQueuePosition.division.numeric,
                                    ( soloQueuePosition.wins.toFloat() / ( soloQueuePosition.wins + soloQueuePosition.losses ).toFloat() ).takeUnless { it.isNaN() }?: 0f,
                                    playerModel.summonerSpellD.blockingSingle().icon.blockingSingle(),
                                    playerModel.summonerSpellF.blockingSingle().icon.blockingSingle(),
                                    playerModel.primaryRunePath.iconResId,
                                    playerModel.secondaryRunePath.iconResId
                                )
                            }
                    )
                }
        )
    }

    private fun getSkillForChampion( summoner: SummonerModel, champion: ChampionModel ) =
        summoner.masteries.blockingSingle().find {
            val mastery = it.blockingSingle()
            mastery.champion.blockingSingle().id == champion.id
        }?.blockingSingle()?.points?: 0

    @MainThread
    private fun updateDuration() {
        val currentMatch = currentMatchLive.value
        if( currentMatch != null ) {
            val durMillis = GregorianCalendar.getInstance().timeInMillis - currentMatch.creationMoment.timeInMillis
            val hours = durMillis / MILLIS_IN_HOUR
            val minutes = durMillis / MILLIS_IN_MINUTE - hours * MINUTES_IN_HOUR
            val seconds = durMillis / MILLIS_IN_SECOND - ( hours * MINUTES_IN_HOUR + minutes ) * SECONDS_IN_MINUTE
            durationTextLive.value = if( hours > 0 ) Formatter().format( DUR_FMT_HMS, hours, minutes, seconds ).toString() else Formatter().format( DUR_FMT_MS, minutes, seconds ).toString()
        }
    }

    override fun onCleared() {
        allDisposables.dispose()
        super.onCleared()
    }
}