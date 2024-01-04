package com.tsarsprocket.reportmid.data_dragon_api.data

import com.tsarsprocket.reportmid.lol.model.Champion
import com.tsarsprocket.reportmid.lol.model.Item
import com.tsarsprocket.reportmid.lol.model.Perk
import com.tsarsprocket.reportmid.lol.model.RunePath
import com.tsarsprocket.reportmid.lol.model.SummonerSpell
import io.reactivex.subjects.ReplaySubject

interface DataDragon {
    val tailSubject: ReplaySubject<Tail>
    val tail: Tail

    interface Tail {
        val latestVersion: String
        val language: String
        val runePaths: List<RunePath>
        val perks: List<Perk>
        val champs: List<Champion>
        val summonerSpells: List<SummonerSpell>
        val items: List<Item>
        fun getRunePathById(id: Int): RunePath
        fun getPerkById(id: Int): Perk
        fun getChampionById(id: Int): Champion
        fun getSummonerSpellById(id: Long): SummonerSpell
        fun getItemById(id: Int): Item
    }
}