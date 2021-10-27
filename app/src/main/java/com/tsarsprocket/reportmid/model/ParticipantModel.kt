package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.riotapi.matchV4.ParticipantDto
import com.tsarsprocket.reportmid.riotapi.matchV4.ParticipantIdentityDto
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import com.tsarsprocket.reportmid.summoner.model.SummonerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.Single

class ParticipantModel @AssistedInject constructor(
    @Assisted val team: TeamModel,
    @Assisted dto: ParticipantDto,
    @Assisted identityDto: ParticipantIdentityDto,
    @Assisted region: RegionModel,
    val repository: Repository,
    summonerRepository: SummonerRepository,
) {
    val accountId = identityDto.player.accountId
    val summoner: Single<SummonerModel> by lazy { summonerRepository.getByAccountId( accountId, region ).cache() }
    val champion = repository.dataDragon.tail.getChampionById(dto.championId)
    val kills = dto.stats.kills
    val deaths = dto.stats.deaths
    val assists = dto.stats.assists
    val isWinner = dto.stats.win
    val creepScore = dto.stats.totalMinionsKilled
    val summonerSpellD by lazy { repository.dataDragon.tail.getSummonerSpellById(dto.spell1Id) }
    val summonerSpellF by lazy { repository.dataDragon.tail.getSummonerSpellById(dto.spell2Id) }
    val items = listOf( dto.stats.item0, dto.stats.item1, dto.stats.item2, dto.stats.item3, dto.stats.item4, dto.stats.item5, dto.stats.item6 )
        .map { if (it != 0L) try { repository.dataDragon.tail.getItemById(it) } catch(ex: RuntimeException) { null } else null } // TODO: Support Ornn items
    val perks = with(dto.stats) {  listOf( perk0, perk1, perk2, perk3, perk4, perk5, statPerk0, statPerk1, statPerk2 ) }.map { repository.dataDragon.tail.getPerkById(it) }
    val runes = perks.mapNotNull { it as? RuneModel }
    val primaryRune = perks.find { it is RuneModel && it.slotNo == 0 } as RuneModel
    val secondaryRunePath = ( perks.find { it is RuneModel && it.runePath.id != primaryRune.runePath.id } as RuneModel ).runePath
}
