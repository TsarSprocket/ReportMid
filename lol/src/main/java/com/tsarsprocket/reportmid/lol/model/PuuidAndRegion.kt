package com.tsarsprocket.reportmid.lol.model

import android.os.Parcel
import android.os.Parcelable

data class PuuidAndRegion(val puuid: Puuid, val region: Region) : Parcelable {

    constructor(puuid: String, regionTag: String) : this(Puuid(puuid), Region.getByTag(regionTag))

    constructor(parcel: Parcel) : this(
        Puuid(parcel.readString().orEmpty()),
        Region.getByTag(parcel.readString()!!)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(puuid.value)
        parcel.writeString(region.tag)
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

        val NONE = PuuidAndRegion(Puuid.EMPTY, Region.RUSSIA)
    }
}
