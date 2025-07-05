package com.tsarsprocket.reportmid.profileOverviewImpl.domain

import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.api.model.Puuid
import com.tsarsprocket.reportmid.lol.api.model.PuuidAndRegion
import com.tsarsprocket.reportmid.lol.api.model.Region
import com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository
import javax.inject.Inject

internal class ProfileOverviewUseCaseImpl @Inject constructor(
    private val summonerRepository: SummonerRepository,
    private val dataDragon: DataDragon,
) : ProfileOverviewUseCase {

    private val dragonTail
        get() = dataDragon.tail

    override suspend fun getOverview(puuid: String, region: Region): ProfileOverviewModel {
        val account = summonerRepository.getRiotAccountByPuuid(Puuid(puuid), region)
        val summoner = summonerRepository.requestRemoteSummonerByPuuidAndRegion(PuuidAndRegion(puuid = Puuid(puuid), region = region))
        val masteries = summonerRepository.requestMasteriesByPuuidAndRegion(Puuid(puuid), region)
            .take(MASTERIES_COUNT)
            .map {
                val iconName = dragonTail.getChampionById(it.championId).iconName
                val championName = dragonTail.getChampionById(it.championId).name

                MasteryOverview(
                    championImageUrl = dragonTail.getChampionImageUrl(iconName),
                    championName = championName,
                    level = it.level,
                    points = it.points,
                )
            }

        return ProfileOverviewModel(
            gameName = account.gameName.value,
            tagLine = account.tagLine.value,
            imageUrl = dataDragon.tail.getSummonerImageUrl(summoner.iconId),
            level = summoner.level,
            masteries = masteries,
        )
    }

    private companion object {
        const val MASTERIES_COUNT = 5
    }
}