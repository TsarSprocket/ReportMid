package com.tsarsprocket.reportmid.profileOverviewImpl.viewIntent

import android.os.Parcelable
import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.profileOverviewImpl.domain.MasteryOverview
import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import kotlinx.parcelize.Parcelize

internal sealed interface ProfileOverviewInternalIntentCluster : ViewIntent {

    @Intent
    @Parcelize
    data class ShowErrorViewIntent(
        val summonerPuuid: String,
        val summonerRegion: Region,
    ) : ProfileOverviewInternalIntentCluster

    @Intent
    @Parcelize
    data class ShowProfileViewIntent(
        val profileImage: String,
        val gameName: String,
        val tagLine: String,
        val level: Int,
        val masteries: List<Mastery>,
    ) : ProfileOverviewInternalIntentCluster {

        @Parcelize
        data class Mastery(
            val championImageUrl: String,
            val championName: String,
            val level: Int,
            val points: Int,
        ) : Parcelable {

            constructor(masteryOverview: MasteryOverview) : this(
                championImageUrl = masteryOverview.championImageUrl,
                championName = masteryOverview.championName,
                level = masteryOverview.level,
                points = masteryOverview.points,
            )
        }
    }
}