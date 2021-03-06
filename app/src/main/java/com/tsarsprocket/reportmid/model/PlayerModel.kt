package com.tsarsprocket.reportmid.model

import android.graphics.drawable.Drawable
import com.tsarsprocket.reportmid.riotapi.spectatorV4.CurrentGameParticipant
import io.reactivex.Single

class PlayerModel(private val repository: Repository, info: CurrentGameParticipant, region: RegionModel) {
    val champion: Single<ChampionModel> = repository.getChampionById(info.championId).cache()
    val summoner: Single<SummonerModel> = repository.getSummonerById(region, info.summonerId)
    val profileIcon: Single<Drawable> = repository.iconProvider.getProfileIcon(info.profileIconId.toInt())
    val summonerSpellD: SummonerSpellModel? = repository.dataDragon.tail.getSummonerSpellById(info.spell1Id)
    val summonerSpellF: SummonerSpellModel? = repository.dataDragon.tail.getSummonerSpellById(info.spell2Id)
    val isBot = info.bot
    val allPerks: List<PerkModel> = info.perks.perkIds.mapNotNull { try { repository.dataDragon.tail.getPerkById(it.toInt()) } catch (ex: Exception) { null } }
    val runes: List<RuneModel> = allPerks.mapNotNull { it as? RuneModel }
    val primaryRune: RuneModel? = runes.find { it.slotNo == 0 }
    val primaryRunePath: RunePathModel? = primaryRune?.runePath
    val secondaryRunePath: RunePathModel? = runes.firstOrNull() { it.runePath != primaryRunePath }?.runePath
}
