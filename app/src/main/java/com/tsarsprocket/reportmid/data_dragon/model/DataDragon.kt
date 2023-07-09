package com.tsarsprocket.reportmid.data_dragon.model

import com.tsarsprocket.reportmid.model.ChampionModel
import com.tsarsprocket.reportmid.model.ItemModel
import com.tsarsprocket.reportmid.model.PerkModel
import com.tsarsprocket.reportmid.model.RunePathModel
import com.tsarsprocket.reportmid.model.SummonerSpellModel
import io.reactivex.subjects.ReplaySubject

interface DataDragon {
    val tailSubject: ReplaySubject<Tail>
    val tail: Tail

    interface Tail {
        val latestVersion: String
        val language: String
        val runePaths: List<RunePathModel>
        val perks: List<PerkModel>
        val champs: List<ChampionModel>
        val summonerSpells: List<SummonerSpellModel>
        val items: List<ItemModel>
        fun getRunePathById(id: Int): RunePathModel
        fun getPerkById(id: Int): PerkModel
        fun getChampionById(id: Int): ChampionModel
        fun getSummonerSpellById(id: Long): SummonerSpellModel
        fun getItemById(id: Int): ItemModel
    }
}