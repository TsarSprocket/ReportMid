package com.tsarsprocket.reportmid.data_dragon_room

interface DataDragonStoragePart {
    // Data Dragon
    fun ddragonVersionDao(): VersionDao
    fun ddragonLanguageDao(): LanguageDao
    fun runePathDao(): RunePathDao
    fun runeDao(): RuneDao
    fun championDao(): ChampionDao
    fun summonerSpellDao(): SummonerSpellDao
    fun itemDao(): ItemDao
}