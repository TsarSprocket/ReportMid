package com.tsarsprocket.reportmid.model

import android.os.Parcel
import android.os.Parcelable

data class PuuidAndRegion(val puuid: String, val region: RegionModel): Parcelable{

    constructor(puuid: String, regionTag: String): this(puuid,RegionModel.getByTag(regionTag))

    constructor(parcel: Parcel) : this(parcel.readString()!!,RegionModel.getByTag(parcel.readString()!!))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(puuid)
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

        val NONE = PuuidAndRegion("", RegionModel.RUSSIA)
    }
}
