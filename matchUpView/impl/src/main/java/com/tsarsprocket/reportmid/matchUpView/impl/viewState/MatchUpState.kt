package com.tsarsprocket.reportmid.matchUpView.impl.viewState

import android.os.Parcel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.tsarsprocket.reportmid.kspApi.annotation.State
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
@State
internal class MatchUpState(
    val puuid: String,
    val region: Region,
    val teams: Map<Int, TeamInfo>,
    initialSelectedTeamIndex: Int,
    val lastLoadedAt: Long = System.currentTimeMillis(),
) : InternalViewState {

    @IgnoredOnParcel
    var selectedTeamIndex: Int by mutableIntStateOf(initialSelectedTeamIndex)

    companion object : Parceler<MatchUpState> {

        @Suppress("DEPRECATION")
        override fun create(parcel: Parcel): MatchUpState {
            val puuid = parcel.readString()!!
            val region = Region.getById(parcel.readInt())
            val teamsSize = parcel.readInt()
            val teams = buildMap<Int, TeamInfo>(teamsSize) {
                repeat(teamsSize) {
                    val id = parcel.readInt()
                    put(id, parcel.readParcelable<TeamInfo>(TeamInfo::class.java.classLoader)!!)
                }
            }
            val selectedTeamIndex = parcel.readInt()
            val lastLoadedAt = parcel.readLong()
            return MatchUpState(
                puuid = puuid,
                region = region,
                teams = teams,
                initialSelectedTeamIndex = selectedTeamIndex,
                lastLoadedAt = lastLoadedAt,
            )
        }

        override fun MatchUpState.write(parcel: Parcel, flags: Int) {
            parcel.writeString(puuid)
            parcel.writeInt(region.id)
            parcel.writeInt(teams.size)
            teams.forEach { (id, team) ->
                parcel.writeInt(id)
                parcel.writeParcelable(team, flags)
            }
            parcel.writeInt(selectedTeamIndex)
            parcel.writeLong(lastLoadedAt)
        }
    }
}
