package com.tsarsprocket.reportmid.dataDragonApi.data

import com.tsarsprocket.reportmid.lol.api.domain.model.Champion
import com.tsarsprocket.reportmid.lol.api.domain.model.KnownItem
import com.tsarsprocket.reportmid.lol.api.domain.model.Perk
import com.tsarsprocket.reportmid.lol.api.domain.model.Rune
import com.tsarsprocket.reportmid.lol.api.domain.model.RunePath
import com.tsarsprocket.reportmid.lol.api.domain.model.SummonerSpell

interface DataDragon {
    val tail: Tail

    suspend fun initialize()

    interface Tail {
        val version: String
        val language: String
        val runePaths: List<RunePath>
        val perks: List<Perk>
        val champs: List<Champion>
        val summonerSpells: List<SummonerSpell>
        val items: List<KnownItem>
        fun getRunePathById(id: Int): RunePath
        fun getPerkById(id: Int): Perk
        fun getChampionById(id: Long): Champion
        fun getSummonerSpellById(id: Long): SummonerSpell
        fun getItemById(itemId: Int): KnownItem

        // Image url formatters
        fun getChampionImageUrl(championName: String): String
        fun getItemImageUrl(item: KnownItem): String
        fun getRuneImageUrl(rune: Rune): String
        fun getRunePathImageUrl(runePath: RunePath): String
        fun getSummonerImageUrl(summonerIconId: Int): String
        fun getSummonerSpellImageUrl(imageName: String): String
    }

    companion object {
        const val IMAGE_INFIX = "img"
        const val BASE_URL = "https://ddragon.leagueoflegends.com/"
        const val CHAMPION_IMAGE_INFIX = "champion"
        const val ITEM_IMAGE_INFIX = "item"
        const val PROFILE_IMAGE_EXT = ".png"
        const val PROFILE_IMAGE_INFIX = "profileicon"
        const val SUMMONER_SPELL_INFIX = "spell"
    }
}