package com.tsarsprocket.reportmid.profileOverviewImpl.viewState

import android.os.Parcelable
import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.viewStateApi.viewState.ViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize

internal sealed interface ProfileOverviewStateCluster : ViewState {

    @State
    @Parcelize
    data class ShowErrorState(
        val summonerPuuid: String,
        val summonerRegion: Region,
    ) : ProfileOverviewStateCluster

    @State
    @Parcelize
    data class ProfileState(
        val icon: String,
        val name: String,
        val tagLine: String,
        val level: Int,
        val masteries: ImmutableList<Mastery>,
    ) : ProfileOverviewStateCluster {

        @Parcelize
        data class Mastery(
            val championImageUrl: String,
            val championName: String,
            val level: String,
            val points: String,
        ) : Parcelable
    }

    @State
    @Parcelize
    data object LoadingState : ProfileOverviewStateCluster
}
