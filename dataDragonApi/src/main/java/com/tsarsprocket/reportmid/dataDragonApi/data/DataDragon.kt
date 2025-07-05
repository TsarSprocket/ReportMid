package com.tsarsprocket.reportmid.dataDragonApi.data

import com.tsarsprocket.reportmid.lol.api.model.Champion
import com.tsarsprocket.reportmid.lol.api.model.Item
import com.tsarsprocket.reportmid.lol.api.model.Perk
import com.tsarsprocket.reportmid.lol.api.model.RunePath
import com.tsarsprocket.reportmid.lol.api.model.SummonerSpell

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
        val items: List<Item>
        fun getRunePathById(id: Int): RunePath
        fun getPerkById(id: Int): Perk
        fun getChampionById(id: Long): Champion
        fun getSummonerSpellById(id: Long): SummonerSpell
        fun getItemById(id: Int): Item

        // Image url formatters
        fun getChampionImageUrl(championName: String): String
        fun getSummonerImageUrl(summonerIconId: Int): String
    }

    companion object {
        private const val IMAGE_INFIX = "img/"
        const val BASE_URL = "https://ddragon.leagueoflegends.com/"
        const val CHAMPION_IMAGE_INFIX = "${IMAGE_INFIX}champion/"
        const val PROFILE_IMAGE_EXT = ".png"
        const val PROFILE_IMAGE_INFIX = "${IMAGE_INFIX}profileicon/"
    }
}