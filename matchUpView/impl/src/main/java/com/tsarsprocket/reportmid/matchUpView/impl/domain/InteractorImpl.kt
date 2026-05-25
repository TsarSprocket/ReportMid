package com.tsarsprocket.reportmid.matchUpView.impl.domain

import com.tsarsprocket.reportmid.currentGameData.api.data.CurrentGameDataRepository
import com.tsarsprocket.reportmid.currentGameData.api.data.model.CurrentGame
import com.tsarsprocket.reportmid.lol.api.domain.model.Puuid
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.CurrentMatchUp
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.MatchUpDomainModel
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.NoMatchUpFound
import com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository
import com.tsarsprocket.reportmid.summonerApi.model.RiotAccount
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class InteractorImpl @Inject constructor(
    private val currentGameDataRepository: CurrentGameDataRepository,
    private val summonerRepository: SummonerRepository,
    private val domainMapper: DomainMapper,
) : Interactor {

    override suspend fun getCurrentMatchUp(puuid: String, region: Region): MatchUpDomainModel {
        return when (val data = currentGameDataRepository.getCurrentGameData(puuid, region)) {
            is CurrentGame.InProgress -> processInProgress(data, region)
            CurrentGame.NotFound -> NoMatchUpFound
        }
    }

    private suspend fun processInProgress(data: CurrentGame.InProgress, region: Region): CurrentMatchUp {
        val riotAccounts: Map<String, RiotAccount> = coroutineScope {
            data.teams
                .flatMap { it.participants }
                .map { participant ->
                    async {
                        participant.puuid to summonerRepository.getRiotAccountByPuuid(
                            puuid = Puuid(participant.puuid),
                            region = region,
                        )
                    }
                }
                .awaitAll()
                .toMap()
        }
        return domainMapper.map(data, riotAccounts)
    }
}
