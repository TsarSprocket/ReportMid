package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.data_dragon.model.DataDragonImpl
import com.tsarsprocket.reportmid.riotapi.matchV5.ParticipantDto
import com.tsarsprocket.reportmid.riotapi.matchV5.PerkStyleDto
import com.tsarsprocket.reportmid.summoner.model.SummonerModel
import com.tsarsprocket.reportmid.summoner.model.SummonerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.Single

class ParticipantModel @AssistedInject constructor(
    @Assisted val team: TeamModel,
    @Assisted participantDto: ParticipantDto,
    @Assisted val region: RegionModel,
    private val dataDragon: DataDragonImpl,
    val repository: Repository,
    summonerRepository: SummonerRepository,
) {
    val puuid: String = participantDto.puuid
    val summoner: Single<SummonerModel> by lazy { summonerRepository.getByPuuidAndRegion( PuuidAndRegion(puuid, region) ).cache() }
    val champion: ChampionModel = dataDragon.tail.getChampionById(participantDto.championId)
    val kills: Int = participantDto.kills
    val deaths: Int = participantDto.deaths
    val assists: Int = participantDto.assists
    val isWinner: Boolean = participantDto.win
    val creepScore: Int = participantDto.totalMinionsKilled
    val summonerSpellD: SummonerSpellModel by lazy { dataDragon.tail.getSummonerSpellById(participantDto.summoner1Id) }
    val summonerSpellF: SummonerSpellModel by lazy { dataDragon.tail.getSummonerSpellById(participantDto.summoner2Id) }
    val items = listOf( participantDto.item0, participantDto.item1, participantDto.item2, participantDto.item3, participantDto.item4, participantDto.item5, participantDto.item6 )
        .map { if (it != 0) try { dataDragon.tail.getItemById(it) } catch(ex: RuntimeException) { null } else null } // TODO: Support Ornn items
    val primaryRunes: List<RuneModel>? = participantDto.perks.styles.find { it.description == PerkStyleDto.Description.PRIMARY_STYLE.value }?.selections
        ?.mapNotNull { dataDragon.tail.getPerkById(it.perk) as? RuneModel }
    val primaryRune: RuneModel? = primaryRunes?.find { it.slotNo == 0 }
    val secondaryRunes: List<RuneModel>? = participantDto.perks.styles.find { it.description == PerkStyleDto.Description.SUBSTYLE.value }?.selections
        ?.mapNotNull { dataDragon.tail.getPerkById(it.perk) as? RuneModel }
    val secondaryRunePath: RunePathModel? = secondaryRunes?.getOrNull(0)?.runePath
}
