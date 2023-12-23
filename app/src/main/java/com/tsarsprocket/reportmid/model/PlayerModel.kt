package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.lol.model.Champion
import com.tsarsprocket.reportmid.lol.model.Perk
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lol.model.Rune
import com.tsarsprocket.reportmid.lol.model.RunePath
import com.tsarsprocket.reportmid.lol.model.SummonerSpell
import com.tsarsprocket.reportmid.riotapi.spectatorV4.CurrentGameParticipant
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import com.tsarsprocket.reportmid.summoner.model.SummonerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.Single

class PlayerModel @AssistedInject constructor(
    @Assisted info: CurrentGameParticipant,
    @Assisted region: Region,
    private val repository: Repository,
    private val summonerRepository: SummonerRepository,
) {
    val champion: Single<Champion> = repository.getChampionById(info.championId).cache()
    val summoner: Single<SummonerModel> = summonerRepository.getBySummonerId(info.summonerId, region)
    val summonerSpellD: SummonerSpell? = repository.dataDragon.tail.getSummonerSpellById(info.spell1Id)
    val summonerSpellF: SummonerSpell? = repository.dataDragon.tail.getSummonerSpellById(info.spell2Id)
    val isBot = info.bot
    val allPerks: List<Perk> = info.perks.perkIds.mapNotNull {
        try {
            repository.dataDragon.tail.getPerkById(it.toInt())
        } catch(ex: Exception) {
            null
        }
    }
    val runes: List<Rune> = allPerks.mapNotNull { it as? Rune }
    val primaryRune: Rune? = runes.find { it.slotNo == 0 }
    val primaryRunePath: RunePath? = primaryRune?.runePath
    val secondaryRunePath: RunePath? = runes.firstOrNull() { it.runePath != primaryRunePath }?.runePath
}
