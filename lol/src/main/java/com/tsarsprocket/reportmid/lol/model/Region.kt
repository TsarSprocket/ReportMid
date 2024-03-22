package com.tsarsprocket.reportmid.lol.model

import com.tsarsprocket.reportmid.lol.model.Server.BR1
import com.tsarsprocket.reportmid.lol.model.Server.EUN1
import com.tsarsprocket.reportmid.lol.model.Server.EUW1
import com.tsarsprocket.reportmid.lol.model.Server.JP1
import com.tsarsprocket.reportmid.lol.model.Server.KR
import com.tsarsprocket.reportmid.lol.model.Server.LA1
import com.tsarsprocket.reportmid.lol.model.Server.LA2
import com.tsarsprocket.reportmid.lol.model.Server.NA1
import com.tsarsprocket.reportmid.lol.model.Server.OC1
import com.tsarsprocket.reportmid.lol.model.Server.RU
import com.tsarsprocket.reportmid.lol.model.Server.TR1
import com.tsarsprocket.reportmid.lol.model.SuperServer.AMERICAS
import com.tsarsprocket.reportmid.lol.model.SuperServer.ASIA
import com.tsarsprocket.reportmid.lol.model.SuperServer.EUROPE

enum class Region(
    val id: Long,
    val tag: String,
    val title: String,
    val server: Server,
    val superServer: SuperServer,
) {
    BRAZIL(0, com.merakianalytics.orianna.types.common.Region.BRAZIL.tag, "Brazil", BR1, AMERICAS),
    EUROPE_NORTH_EAST(1, com.merakianalytics.orianna.types.common.Region.EUROPE_NORTH_EAST.tag, "Europe North East", EUN1, EUROPE),
    EUROPE_WEST(2, com.merakianalytics.orianna.types.common.Region.EUROPE_WEST.tag, "Europe West", EUW1, EUROPE),
    JAPAN(3, com.merakianalytics.orianna.types.common.Region.JAPAN.tag, "Japan", JP1, ASIA),
    KOREA(4, com.merakianalytics.orianna.types.common.Region.KOREA.tag, "Korea", KR, ASIA),
    LATIN_AMERICA_NORTH(5, com.merakianalytics.orianna.types.common.Region.LATIN_AMERICA_NORTH.tag, "Latin America North", LA1, AMERICAS),
    LATIN_AMERICA_SOUTH(6, com.merakianalytics.orianna.types.common.Region.LATIN_AMERICA_SOUTH.tag, "Latin America South", LA2, AMERICAS),
    NORTH_AMERICA(7, com.merakianalytics.orianna.types.common.Region.NORTH_AMERICA.tag, "North America", NA1, AMERICAS),
    OCEANIA(8, com.merakianalytics.orianna.types.common.Region.OCEANIA.tag, "Oceania", OC1, AMERICAS),
    RUSSIA(9, com.merakianalytics.orianna.types.common.Region.RUSSIA.tag, "Russia", RU, EUROPE),
    TURKEY(10, com.merakianalytics.orianna.types.common.Region.TURKEY.tag, "Turkey", TR1, EUROPE),
    ;

    companion object {
        val byId = entries.sortedBy { it.id }
        val byTag = entries.associateBy { it.tag }

        fun getById(id: Long) = byId[id.toInt()]
        fun getByTag(tag: String) = byTag[tag] ?: throw RuntimeException("No region found for tag $tag")
    }
}