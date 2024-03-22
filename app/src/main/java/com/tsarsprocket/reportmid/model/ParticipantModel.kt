package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.data_dragon_impl.data.DataDragonImpl
import com.tsarsprocket.reportmid.lol.model.Champion
import com.tsarsprocket.reportmid.lol.model.Puuid
import com.tsarsprocket.reportmid.lol.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.model.Region
import com.tsarsprocket.reportmid.lol.model.Rune
import com.tsarsprocket.reportmid.lol.model.RunePath
import com.tsarsprocket.reportmid.lol.model.SummonerSpell
import com.tsarsprocket.reportmid.riotapi.matchV5.ParticipantDto
import com.tsarsprocket.reportmid.riotapi.matchV5.PerkStyleDto
import com.tsarsprocket.reportmid.summoner_api.model.Summoner
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.Single
import kotlinx.coroutines.rx2.rxSingle

class ParticipantModel @AssistedInject constructor(
    @Assisted val team: TeamModel,
    @Assisted participantDto: ParticipantDto,
    @Assisted val region: Region,
    private val dataDragon: DataDragonImpl,
    val repository: Repository,
    summonerRepository: com.tsarsprocket.reportmid.summoner_api.data.SummonerRepository,
) {
    val puuid: Puuid = Puuid(participantDto.puuid)
    val summoner: Single<Summoner> by lazy { rxSingle { summonerRepository.requestRemoteSummonerByPuuidAndRegion(PuuidAndRegion(puuid, region)) }.cache() }
    val champion: Champion = dataDragon.tail.getChampionById(participantDto.championId.toLong())
    val kills: Int = participantDto.kills
    val deaths: Int = participantDto.deaths
    val assists: Int = participantDto.assists
    val isWinner: Boolean = participantDto.win
    val creepScore: Int = participantDto.totalMinionsKilled
    val summonerSpellD: SummonerSpell by lazy { dataDragon.tail.getSummonerSpellById(participantDto.summoner1Id) }
    val summonerSpellF: SummonerSpell by lazy { dataDragon.tail.getSummonerSpellById(participantDto.summoner2Id) }
    val items = listOf(participantDto.item0, participantDto.item1, participantDto.item2, participantDto.item3, participantDto.item4, participantDto.item5, participantDto.item6)
        .map {
            if(it != 0) try {
                dataDragon.tail.getItemById(it)
            } catch(ex: RuntimeException) {
                null
            } else null
        } // TODO: Support Ornn items
    val primaryRunes: List<Rune>? = participantDto.perks.styles.find { it.description == PerkStyleDto.Description.PRIMARY_STYLE.value }?.selections
        ?.mapNotNull { dataDragon.tail.getPerkById(it.perk) as? Rune }
    val primaryRune: Rune? = primaryRunes?.find { it.slotNo == 0 }
    val secondaryRunes: List<Rune>? = participantDto.perks.styles.find { it.description == PerkStyleDto.Description.SUBSTYLE.value }?.selections
        ?.mapNotNull { dataDragon.tail.getPerkById(it.perk) as? Rune }
    val secondaryRunePath: RunePath? = secondaryRunes?.getOrNull(0)?.runePath
}
