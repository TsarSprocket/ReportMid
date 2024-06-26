package com.tsarsprocket.reportmid.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.tsarsprocket.reportmid.RIOTIconProvider
import com.tsarsprocket.reportmid.leaguePositionApi.data.LeaguePositionRepository
import com.tsarsprocket.reportmid.lol.model.Champion
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.model.CurrentMatchModel
import com.tsarsprocket.reportmid.model.CurrentMatchTeamModel
import com.tsarsprocket.reportmid.model.Repository
import com.tsarsprocket.reportmid.presentation.PlayerPresentation
import com.tsarsprocket.reportmid.summonerApi.model.Summoner
import com.tsarsprocket.reportmid.utils.common.logError
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.coroutines.rx2.rxObservable
import kotlinx.coroutines.rx2.rxSingle
import java.util.Formatter
import java.util.GregorianCalendar
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

class MatchupViewModel @Inject constructor(
    private val repository: Repository,
    private val summonerRepository: com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository,
    private val leaguePositionRepository: LeaguePositionRepository, // temporary, until migrated to new screen
    private val iconProvider: RIOTIconProvider, // temporary, until migrated to new screen
): ViewModel() {

    lateinit var puuidAndRegion: PuuidAndRegion

    var summoner = MutableLiveData<Summoner>()

    val matchInProgress = MutableLiveData<Boolean>()

    val currentMatchLive = MutableLiveData<CurrentMatchModel>()

    val gameTypeTextLive = currentMatchLive.map { match -> repository.context.getString(match.gameType.titleResId) }
    val durationTextLive = MutableLiveData<String>( STR_NO_DURATION )

    val blueTeamParticipants = MutableLiveData<List<PlayerPresentation>>()
    val redTeamParticipants = MutableLiveData<List<PlayerPresentation>>()

    val allDisposables = CompositeDisposable()
    val matchDisposables = CompositeDisposable()

    init {
        allDisposables.add( Observable.interval( 1, TimeUnit.SECONDS ).observeOn( AndroidSchedulers.mainThread() ).subscribe{
            updateDuration()
        } )
    }

    fun loadForSummoner( puuidAndRegion: PuuidAndRegion) {
        allDisposables.add(rxObservable<Summoner> { summonerRepository.requestRemoteSummonerByPuuidAndRegion(puuidAndRegion) }
            .switchMap { summoner.value = it; repository.getCurrentMatch(it) }
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
        curTeamModel: CurrentMatchTeamModel,
        participantsListLive: MutableLiveData<List<PlayerPresentation>>,
        disposer: CompositeDisposable
    ) {
        participantsListLive.postValue(
            curTeamModel.participants
                .map { playerModel ->
                    val playerPresentation = PlayerPresentation()
                    disposer.addAll(
                        playerModel.summoner.subscribe({ sum -> playerPresentation.summoner.postValue(sum) },
                            { ex -> this.logError("Can't obtain icon for summoner", ex) }),
                        playerModel.champion.flatMap { iconProvider.getChampionIcon(it.iconName) }.subscribe({ drawable -> playerPresentation.championIconLive.postValue(drawable) },
                            { ex -> this.logError("Can't obtain icon for champion", ex) }),
                        Single.zip(
                            playerModel.champion,
                            playerModel.summoner,
                            BiFunction<Champion, Summoner, Single<Int>> { ch, su -> getSkillForChampion(su, ch) })
                            .flatMap { it }
                            .concatWith(Single.just(-1))
                            .take(1)
                            .subscribe({ playerPresentation.summonerChampionSkillLive.postValue(it) },
                                { ex -> this.logError("Cannot calculate champion skill", ex) }),
                        playerModel.summoner.subscribe(
                            { sum -> playerPresentation.summonerNameLive.postValue(sum.name); playerPresentation.summonerLevelLive.postValue(sum.level) },
                            { ex -> this.logError("Can't get summoner", ex) }),
                        playerModel.summoner.flatMap { rxSingle { leaguePositionRepository.getSoloQueueLeaguePosition(it.riotId, it.region)!! /* temporary*/ } }
                            .subscribe({ soloQueuePosition ->
                                playerPresentation.soloqueueRankLive.postValue(soloQueuePosition.tier.abbreviation + soloQueuePosition.division.numeric)
                                playerPresentation.soloqueueWinrateLive.postValue((soloQueuePosition.wins.toFloat() / (soloQueuePosition.wins + soloQueuePosition.losses).toFloat()).takeUnless { it.isNaN() }
                                    ?: 0f)
                            },
                                { ex -> this.logError("Can't obtain soloqueue position", ex) }),
                        playerModel.summonerSpellD?.let { iconProvider.getSummonerSpellIcon(it.iconName) }?.subscribe { drawable -> playerPresentation.summonerSpellDLive.postValue(drawable) },
                        playerModel.summonerSpellF?.let { iconProvider.getSummonerSpellIcon(it.iconName) }?.subscribe { drawable -> playerPresentation.summonerSpellFLive.postValue(drawable) },
                        playerModel.primaryRune?.let { runeModel ->
                            iconProvider.getRuneIcon(runeModel.iconName).subscribe({ drawable -> playerPresentation.primaryRuneIconLive.postValue(drawable) },
                                { ex -> this.logError("Error getting primary rune path", ex) })
                        },
                        playerModel.secondaryRunePath?.let { runePathModel ->
                            iconProvider.getRunePathIcon(runePathModel.iconPath).subscribe({ drawable -> playerPresentation.secondaryRunePathIconLive.postValue(drawable) },
                                { ex -> logError("Error getting secondary rune path", ex) })
                        },
                    )

                    playerPresentation
                }
        )
    }

    private fun getSkillForChampion(summoner: Summoner, champion: Champion) =
        summoner.getMasteryWithChampion(champion).map { it.points }.first(0)

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
        allDisposables.clear()
        super.onCleared()
    }
}