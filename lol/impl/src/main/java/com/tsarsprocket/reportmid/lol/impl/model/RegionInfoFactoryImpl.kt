package com.tsarsprocket.reportmid.lol.impl.model

import android.content.Context
import com.tsarsprocket.reportmid.baseApi.di.AppContext
import com.tsarsprocket.reportmid.lol.api.model.Region.BRAZIL
import com.tsarsprocket.reportmid.lol.api.model.Region.Companion.ID_NONEXISTENT_REGION
import com.tsarsprocket.reportmid.lol.api.model.Region.EUROPE_NORTH_EAST
import com.tsarsprocket.reportmid.lol.api.model.Region.EUROPE_WEST
import com.tsarsprocket.reportmid.lol.api.model.Region.JAPAN
import com.tsarsprocket.reportmid.lol.api.model.Region.KOREA
import com.tsarsprocket.reportmid.lol.api.model.Region.LATIN_AMERICA_NORTH
import com.tsarsprocket.reportmid.lol.api.model.Region.LATIN_AMERICA_SOUTH
import com.tsarsprocket.reportmid.lol.api.model.Region.NORTH_AMERICA
import com.tsarsprocket.reportmid.lol.api.model.Region.OCEANIA
import com.tsarsprocket.reportmid.lol.api.model.Region.RUSSIA
import com.tsarsprocket.reportmid.lol.api.model.Region.TURKEY
import com.tsarsprocket.reportmid.lol.api.model.RegionInfo
import com.tsarsprocket.reportmid.lol.impl.R
import javax.inject.Inject

internal class RegionInfoFactoryImpl @Inject constructor(
    @AppContext appContext: Context,
) : RegionInfo.Factory {

    private val byRegion by lazy {
        listOf(
            RegionInfo(BRAZIL.id, appContext.getString(R.string.lol_impl_region_name_brazil)),
            RegionInfo(EUROPE_NORTH_EAST.id, appContext.getString(R.string.lol_impl_region_name_europe_north_east)),
            RegionInfo(EUROPE_WEST.id, appContext.getString(R.string.lol_impl_region_name_europe_west)),
            RegionInfo(JAPAN.id, appContext.getString(R.string.lol_impl_region_name_japan)),
            RegionInfo(KOREA.id, appContext.getString(R.string.lol_impl_region_name_korea)),
            RegionInfo(LATIN_AMERICA_NORTH.id, appContext.getString(R.string.lol_impl_region_name_latin_america_north)),
            RegionInfo(LATIN_AMERICA_SOUTH.id, appContext.getString(R.string.lol_impl_region_name_latin_america_south)),
            RegionInfo(NORTH_AMERICA.id, appContext.getString(R.string.lol_impl_region_name_north_america)),
            RegionInfo(OCEANIA.id, appContext.getString(R.string.lol_impl_region_name_oceania)),
            RegionInfo(RUSSIA.id, appContext.getString(R.string.lol_impl_region_name_russia)),
            RegionInfo(TURKEY.id, appContext.getString(R.string.lol_impl_region_name_turkey)),
        ).associateBy { it.regionId }.withDefault { RegionInfo(ID_NONEXISTENT_REGION, appContext.getString(R.string.lol_impl_region_name_nonexistent)) }
    }

    override fun get(regionId: Int): RegionInfo = byRegion[regionId]!!
}