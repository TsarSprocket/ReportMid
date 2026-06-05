package com.tsarsprocket.reportmid.matchUpView.impl.viewState

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tsarsprocket.reportmid.viewStateApi.viewState.LoadablePart
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
internal class ParticipantInfo(
    val puuid: String,
    val championImageUrl: String,
    account: LoadablePart<AccountInfo>,
    summoner: LoadablePart<SummonerInfo>,
    val primaryRuneImageUrls: List<String>,
    val secondaryRuneImageUrls: List<String>,
    val summonerSpell1ImageUrl: String,
    val summonerSpell2ImageUrl: String,
) : Parcelable {

    @IgnoredOnParcel
    var account: LoadablePart<AccountInfo> by mutableStateOf(account)

    @IgnoredOnParcel
    var summoner: LoadablePart<SummonerInfo> by mutableStateOf(summoner)

    companion object : Parceler<ParticipantInfo> {

        @Suppress("DEPRECATION")
        override fun create(parcel: Parcel): ParticipantInfo = ParticipantInfo(
            puuid = parcel.readString()!!,
            championImageUrl = parcel.readString()!!,
            account = parcel.readParcelable(LoadablePart::class.java.classLoader)!!,
            summoner = parcel.readParcelable(LoadablePart::class.java.classLoader)!!,
            primaryRuneImageUrls = parcel.createStringArrayList()!!,
            secondaryRuneImageUrls = parcel.createStringArrayList()!!,
            summonerSpell1ImageUrl = parcel.readString()!!,
            summonerSpell2ImageUrl = parcel.readString()!!,
        )

        override fun ParticipantInfo.write(parcel: Parcel, flags: Int) {
            parcel.writeString(puuid)
            parcel.writeString(championImageUrl)
            parcel.writeParcelable(account, flags)
            parcel.writeParcelable(summoner, flags)
            parcel.writeStringList(primaryRuneImageUrls)
            parcel.writeStringList(secondaryRuneImageUrls)
            parcel.writeString(summonerSpell1ImageUrl)
            parcel.writeString(summonerSpell2ImageUrl)
        }
    }
}
