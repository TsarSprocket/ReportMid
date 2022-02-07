package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.model.ServerModel.*
import com.tsarsprocket.reportmid.model.SuperServerModel.*

enum class RegionModel(
    val tag: String,
    val title: String,
    val server: ServerModel,
    val superServer: SuperServerModel,
) {
    BRAZIL(com.merakianalytics.orianna.types.common.Region.BRAZIL.tag, "Brazil", BR1, AMERICAS),
    EUROPE_NORTH_EAST(com.merakianalytics.orianna.types.common.Region.EUROPE_NORTH_EAST.tag, "Europe North East", EUN1, EUROPE),
    EUROPE_WEST(com.merakianalytics.orianna.types.common.Region.EUROPE_WEST.tag, "Europe West", EUW1, EUROPE),
    JAPAN(com.merakianalytics.orianna.types.common.Region.JAPAN.tag, "Japan", JP1, ASIA),
    KOREA(com.merakianalytics.orianna.types.common.Region.KOREA.tag, "Korea", KR, ASIA),
    LATIN_AMERICA_NORTH(com.merakianalytics.orianna.types.common.Region.LATIN_AMERICA_NORTH.tag, "Latin America North", LA1, AMERICAS),
    LATIN_AMERICA_SOUTH(com.merakianalytics.orianna.types.common.Region.LATIN_AMERICA_SOUTH.tag, "Latin America South", LA2, AMERICAS),
    NORTH_AMERICA(com.merakianalytics.orianna.types.common.Region.NORTH_AMERICA.tag, "North America", NA1, AMERICAS),
    OCEANIA(com.merakianalytics.orianna.types.common.Region.OCEANIA.tag, "Oceania", OC1, AMERICAS),
    RUSSIA(com.merakianalytics.orianna.types.common.Region.RUSSIA.tag, "Russia", RU, EUROPE),
    TURKEY(com.merakianalytics.orianna.types.common.Region.TURKEY.tag, "Turkey", TR1, EUROPE),
    ;

    companion object {
        val byTag = values().associateBy { it.tag }
        fun getByTag(tag: String) = byTag[tag] ?: throw RuntimeException("No region found for tag $tag")
    }
}