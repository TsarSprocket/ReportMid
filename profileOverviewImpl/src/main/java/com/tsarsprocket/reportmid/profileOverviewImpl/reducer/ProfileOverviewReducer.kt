package com.tsarsprocket.reportmid.profileOverviewImpl.reducer

import com.tsarsprocket.reportmid.baseApi.di.PerApi
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.profileOverviewApi.viewIntent.ProfileOverviewViewIntent
import com.tsarsprocket.reportmid.profileOverviewImpl.domain.ProfileOverviewUseCase
import com.tsarsprocket.reportmid.profileOverviewImpl.viewIntent.ProfileOverviewInternalIntentCluster
import com.tsarsprocket.reportmid.profileOverviewImpl.viewIntent.ProfileOverviewInternalIntentCluster.ShowErrorViewIntent
import com.tsarsprocket.reportmid.profileOverviewImpl.viewIntent.ProfileOverviewInternalIntentCluster.ShowProfileViewIntent
import com.tsarsprocket.reportmid.profileOverviewImpl.viewState.ProfileOverviewStateCluster.LoadingState
import com.tsarsprocket.reportmid.profileOverviewImpl.viewState.ProfileOverviewStateCluster.ProfileState
import com.tsarsprocket.reportmid.profileOverviewImpl.viewState.ProfileOverviewStateCluster.ShowErrorState
import com.tsarsprocket.reportmid.utils.common.STANDARD_FIRST_DELAY_MILLIS
import com.tsarsprocket.reportmid.utils.common.STANDARD_RETRY_DELAY_MILLIS
import com.tsarsprocket.reportmid.utils.coroutines.doAtLeast
import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@PerApi
@Reducer(
    explicitIntents = [
        ProfileOverviewViewIntent::class,
    ],
)
internal class ProfileOverviewReducer @Inject constructor(
    private val profileOverviewUseCase: ProfileOverviewUseCase,
) : ViewStateReducer {

    override suspend fun reduce(intent: ViewIntent, state: ViewState, stateHolder: ViewStateHolder): ViewState = when(intent) {
        is ProfileOverviewViewIntent -> stateHolder.loadProfile(puuid = intent.summonerPuuid, region = intent.summonerRegion, isRetry = intent.isRetry)
        is ProfileOverviewInternalIntentCluster -> when(intent) {
            is ShowProfileViewIntent -> showProfile(intent)
            is ShowErrorViewIntent -> ShowErrorState(intent.summonerPuuid, intent.summonerRegion)
        }

        else -> super.reduce(intent, state, stateHolder)
    }

    private fun showProfile(intent: ShowProfileViewIntent): ProfileState {
        return ProfileState(
            icon = intent.profileImage,
            name = intent.gameName,
            tagLine = intent.tagLine,
            level = intent.level,
            masteries = intent.masteries.map {
                ProfileState.Mastery(
                    championImageUrl = it.championImageUrl,
                    championName = it.championName,
                    level = it.level.toString(),
                    points = NumberFormat.getNumberInstance().format(it.points),
                )
            }.toPersistentList(),
        )
    }

    private fun ViewStateHolder.loadProfile(puuid: String, region: Region, isRetry: Boolean): ViewState {
        coroutineScope.launch(CoroutineExceptionHandler { _, _ ->
            postIntent(ShowErrorViewIntent(puuid, region))
        }) {
            val profileOverview = doAtLeast((if(isRetry) STANDARD_RETRY_DELAY_MILLIS else STANDARD_FIRST_DELAY_MILLIS).milliseconds) {
                profileOverviewUseCase.getOverview(puuid, region)
            }

            postIntent(
                with(profileOverview) {
                    ShowProfileViewIntent(
                        profileImage = imageUrl,
                        gameName = gameName,
                        tagLine = tagLine,
                        level = level,
                        masteries = masteries.map { ShowProfileViewIntent.Mastery(it) },
                    )
                }
            )
        }

        return LoadingState
    }
}