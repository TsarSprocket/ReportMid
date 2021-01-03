package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.model.ServerModel.*

enum class RegionModel(
    val tag: String,
    val title: String,
    val shadowRegion: com.merakianalytics.orianna.types.common.Region,
    val server: ServerModel,
) {
    BRAZIL( com.merakianalytics.orianna.types.common.Region.BRAZIL.tag, "Brazil", com.merakianalytics.orianna.types.common.Region.BRAZIL, BR1),
    EUROPE_NORTH_EAST( com.merakianalytics.orianna.types.common.Region.EUROPE_NORTH_EAST.tag, "Europe North East", com.merakianalytics.orianna.types.common.Region.EUROPE_NORTH_EAST, EUN1 ),
    EUROPE_WEST( com.merakianalytics.orianna.types.common.Region.EUROPE_WEST.tag, "Europe West", com.merakianalytics.orianna.types.common.Region.EUROPE_WEST, EUW1 ),
    JAPAN( com.merakianalytics.orianna.types.common.Region.JAPAN.tag, "Japan", com.merakianalytics.orianna.types.common.Region.JAPAN, JP1 ),
    KOREA( com.merakianalytics.orianna.types.common.Region.KOREA.tag, "Korea", com.merakianalytics.orianna.types.common.Region.KOREA, KR ),
    LATIN_AMERICA_NORTH( com.merakianalytics.orianna.types.common.Region.LATIN_AMERICA_NORTH.tag, "Latin America North", com.merakianalytics.orianna.types.common.Region.LATIN_AMERICA_NORTH, LA1 ),
    LATIN_AMERICA_SOUTH( com.merakianalytics.orianna.types.common.Region.LATIN_AMERICA_SOUTH.tag, "Latin America South", com.merakianalytics.orianna.types.common.Region.LATIN_AMERICA_SOUTH, LA2 ),
    NORTH_AMERICA( com.merakianalytics.orianna.types.common.Region.NORTH_AMERICA.tag, "North America", com.merakianalytics.orianna.types.common.Region.NORTH_AMERICA, NA1 ),
    OCEANIA( com.merakianalytics.orianna.types.common.Region.OCEANIA.tag, "Oceania", com.merakianalytics.orianna.types.common.Region.OCEANIA, OC1 ),
    RUSSIA( com.merakianalytics.orianna.types.common.Region.RUSSIA.tag, "Russia", com.merakianalytics.orianna.types.common.Region.RUSSIA, RU ),
    TURKEY( com.merakianalytics.orianna.types.common.Region.TURKEY.tag, "Turkey", com.merakianalytics.orianna.types.common.Region.TURKEY, TR1 ),
    ;

    companion object {
        val byTag = values().map { it.tag to it }.toMap()
        val byShadowRegion = values().map { it.shadowRegion to it }.toMap()
        fun getByTag(tag: String) = byTag[tag] ?: throw RuntimeException("No region found for tag $tag")
    }
}