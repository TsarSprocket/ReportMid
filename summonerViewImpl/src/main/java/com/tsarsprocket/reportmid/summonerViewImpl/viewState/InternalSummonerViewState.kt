package com.tsarsprocket.reportmid.summonerViewImpl.viewState

import android.os.Parcel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchHistory.api.viewIntent.MatchHistoryIntent
import com.tsarsprocket.reportmid.matchUpView.api.viewIntent.MatchUpIntent
import com.tsarsprocket.reportmid.profileOverviewApi.viewIntent.ProfileOverviewViewIntent
import com.tsarsprocket.reportmid.summonerViewApi.viewState.SummonerViewStateReturnPoint
import com.tsarsprocket.reportmid.summonerViewImpl.viewIntent.ReturnToSummoner
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.LazyViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import com.tsarsprocket.reportmid.viewStateApi.viewmodel.ViewStateHolder
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
@State
internal class InternalSummonerViewState(
    val profileOverviewStateHolder: ViewStateHolder,
    val matchUpStateHolder: ViewStateHolder,
    val matchHistoryStateHolder: ViewStateHolder,
    val summonerPuuid: String,
    val summonerRegion: Region,
    initialActivePage: ActivePage,
) : ViewState, SummonerViewStateReturnPoint {

    @IgnoredOnParcel
    var activePage: ActivePage by mutableStateOf(initialActivePage)

    override fun getRestoreStateIntent(): ViewIntent = ReturnToSummoner(
        puuid = summonerPuuid,
        region = summonerRegion,
        activePage = activePage,
    )

    override fun setParentHolder(parentHolder: ViewStateHolder) {
        profileOverviewStateHolder.setParentHolder(parentHolder)
        matchUpStateHolder.setParentHolder(parentHolder)
        matchHistoryStateHolder.setParentHolder(parentHolder)
    }

    override fun start() {
        // TODO: Change to navigation
        profileOverviewStateHolder.apply {
            start()
            postIntent(ProfileOverviewViewIntent(summonerPuuid, summonerRegion))
        }
        matchUpStateHolder.apply {
            start()
            postIntent(LazyViewIntent(MatchUpIntent(summonerPuuid, summonerRegion)))
        }
        matchHistoryStateHolder.apply {
            start()
            postIntent(LazyViewIntent(MatchHistoryIntent(summonerPuuid, summonerRegion)))
        }
    }

    override fun stop() {
        matchHistoryStateHolder.stop()
        matchUpStateHolder.stop()
        profileOverviewStateHolder.stop()
    }

    companion object : Parceler<InternalSummonerViewState> {

        @Suppress("DEPRECATION")
        override fun create(parcel: Parcel): InternalSummonerViewState {
            val profileOverviewStateHolder = parcel.readParcelable<ViewStateHolder>(ViewStateHolder::class.java.classLoader)!!
            val matchUpStateHolder = parcel.readParcelable<ViewStateHolder>(ViewStateHolder::class.java.classLoader)!!
            val matchHistoryStateHolder = parcel.readParcelable<ViewStateHolder>(ViewStateHolder::class.java.classLoader)!!
            val summonerPuuid = parcel.readString()!!
            val summonerRegion = Region.getById(parcel.readInt())
            val activePage = ActivePage.entries[parcel.readInt()]
            return InternalSummonerViewState(
                profileOverviewStateHolder = profileOverviewStateHolder,
                matchUpStateHolder = matchUpStateHolder,
                matchHistoryStateHolder = matchHistoryStateHolder,
                summonerPuuid = summonerPuuid,
                summonerRegion = summonerRegion,
                initialActivePage = activePage,
            )
        }

        override fun InternalSummonerViewState.write(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(profileOverviewStateHolder, flags)
            parcel.writeParcelable(matchUpStateHolder, flags)
            parcel.writeParcelable(matchHistoryStateHolder, flags)
            parcel.writeString(summonerPuuid)
            parcel.writeInt(summonerRegion.id)
            parcel.writeInt(activePage.ordinal)
        }
    }
}