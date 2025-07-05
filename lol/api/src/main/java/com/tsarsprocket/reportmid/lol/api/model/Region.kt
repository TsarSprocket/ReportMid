package com.tsarsprocket.reportmid.lol.api.model

import com.tsarsprocket.reportmid.lol.api.model.Server.BR1
import com.tsarsprocket.reportmid.lol.api.model.Server.EUN1
import com.tsarsprocket.reportmid.lol.api.model.Server.EUW1
import com.tsarsprocket.reportmid.lol.api.model.Server.JP1
import com.tsarsprocket.reportmid.lol.api.model.Server.KR
import com.tsarsprocket.reportmid.lol.api.model.Server.LA1
import com.tsarsprocket.reportmid.lol.api.model.Server.LA2
import com.tsarsprocket.reportmid.lol.api.model.Server.NA1
import com.tsarsprocket.reportmid.lol.api.model.Server.OC1
import com.tsarsprocket.reportmid.lol.api.model.Server.RU
import com.tsarsprocket.reportmid.lol.api.model.Server.TR1
import com.tsarsprocket.reportmid.lol.api.model.SuperServer.AMERICAS
import com.tsarsprocket.reportmid.lol.api.model.SuperServer.ASIA
import com.tsarsprocket.reportmid.lol.api.model.SuperServer.EUROPE

private const val ID_BRAZIL = 0
private const val ID_EUROPE_NORTH_EAST = 1
private const val ID_EUROPE_WEST = 2
private const val ID_JAPAN = 3
private const val ID_KOREA = 4
private const val ID_LATIN_AMERICA_NORTH = 5
private const val ID_LATIN_AMERICA_SOUTH = 6
private const val ID_NORTH_AMERICA = 7
private const val ID_OCEANIA = 8
private const val ID_RUSSIA = 9
private const val ID_TURKEY = 10

enum class Region(
    val id: Int,
    val server: Server,
    val superServer: SuperServer,
) {
    BRAZIL(ID_BRAZIL, BR1, AMERICAS),
    EUROPE_NORTH_EAST(ID_EUROPE_NORTH_EAST, EUN1, EUROPE),
    EUROPE_WEST(ID_EUROPE_WEST, EUW1, EUROPE),
    JAPAN(ID_JAPAN, JP1, ASIA),
    KOREA(ID_KOREA, KR, ASIA),
    LATIN_AMERICA_NORTH(ID_LATIN_AMERICA_NORTH, LA1, AMERICAS),
    LATIN_AMERICA_SOUTH(ID_LATIN_AMERICA_SOUTH, LA2, AMERICAS),
    NORTH_AMERICA(ID_NORTH_AMERICA, NA1, AMERICAS),
    OCEANIA(ID_OCEANIA, OC1, AMERICAS),
    RUSSIA(ID_RUSSIA, RU, EUROPE),
    TURKEY(ID_TURKEY, TR1, EUROPE);

    companion object {
        const val ID_NONEXISTENT_REGION = -1

        val byId = entries.associateBy { it.id }

        fun getById(id: Int) = byId[id] ?: error("No region with id $id")
    }
}