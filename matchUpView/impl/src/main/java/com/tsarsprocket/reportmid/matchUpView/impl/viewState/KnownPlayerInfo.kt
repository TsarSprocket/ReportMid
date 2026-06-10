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
internal class KnownPlayerInfo(
    val puuid: String,
    account: LoadablePart<AccountInfo>,
    summoner: LoadablePart<SummonerInfo>,
) : PlayerInfo {

    @IgnoredOnParcel
    var account: LoadablePart<AccountInfo> by mutableStateOf(account)

    @IgnoredOnParcel
    var summoner: LoadablePart<SummonerInfo> by mutableStateOf(summoner)

    companion object : Parceler<KnownPlayerInfo> {

        @Suppress("DEPRECATION")
        override fun create(parcel: Parcel): KnownPlayerInfo = KnownPlayerInfo(
            puuid = parcel.readString()!!,
            account = parcel.readParcelable(LoadablePart::class.java.classLoader)!!,
            summoner = parcel.readParcelable(LoadablePart::class.java.classLoader)!!,
        )

        override fun KnownPlayerInfo.write(parcel: Parcel, flags: Int) {
            parcel.writeString(puuid)
            parcel.writeParcelable(account, flags)
            parcel.writeParcelable(summoner, flags)
        }
    }
}
