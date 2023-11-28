package com.tsarsprocket.reportmid.lol.model

enum class Tier(val token: String, abbreviation: String) {
    Unranked(token = "Unranked", abbreviation = "U"),
    Iron(token = "IRON", abbreviation = "I"),
    Bronze(token = "BRONZE", abbreviation = "B"),
    Silver(token = "Silver", abbreviation = "S"),
    Gold(token = "GOLD", abbreviation = "G"),
    Platinum(token = "Platinum", abbreviation = "P"),
    Emerald(token = "EMERALD", abbreviation = "E"),
    Diamond(token = "DIAMOND", abbreviation = "D"),
    Master(token = "MASTER", abbreviation = "M"),
    Grandmaster(token = "GRANDMASTER", abbreviation = "GM"),
    Challenger(token = "CHALLENGER", abbreviation = "Ch"),
    Unknown(token = "unknown", abbreviation = "Unk");

    companion object {

        fun byName(name: String) = name.uppercase().let { capitalized -> entries.find { it.token == capitalized } ?: Unknown }
    }
}
