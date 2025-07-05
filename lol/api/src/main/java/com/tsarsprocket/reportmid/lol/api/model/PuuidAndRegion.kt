package com.tsarsprocket.reportmid.lol.api.model

import android.os.Parcel
import android.os.Parcelable

data class PuuidAndRegion(val puuid: Puuid, val region: Region) : Parcelable {

    constructor(parcel: Parcel) : this(
        Puuid(parcel.readString().orEmpty()),
        Region.getById(parcel.readInt()),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(puuid.value)
        parcel.writeInt(region.id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PuuidAndRegion> {
        override fun createFromParcel(parcel: Parcel): PuuidAndRegion {
            return PuuidAndRegion(parcel)
        }

        override fun newArray(size: Int): Array<PuuidAndRegion?> {
            return arrayOfNulls(size)
        }
    }
}
