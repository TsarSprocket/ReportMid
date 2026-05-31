package com.tsarsprocket.reportmid.matchUpView.impl.domain

import com.tsarsprocket.reportmid.currentGameData.api.data.CurrentGameDataRepository
import com.tsarsprocket.reportmid.currentGameData.api.data.model.CurrentGame
import com.tsarsprocket.reportmid.lol.api.domain.model.Puuid
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.Account
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.CurrentMatchUp
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.MatchUpDomainModel
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.NoMatchUpFound
import com.tsarsprocket.reportmid.summonerApi.data.SummonerRepository
import javax.inject.Inject

internal class InteractorImpl @Inject constructor(
    private val currentGameDataRepository: CurrentGameDataRepository,
    private val summonerRepository: SummonerRepository,
    private val domainMapper: DomainMapper,
) : Interactor {

    override suspend fun getAccount(puuid: String, region: Region): Account {
        val riotAccount = summonerRepository.getRiotAccountByPuuid(Puuid(puuid), region)
        return Account(gameName = riotAccount.gameName, tagLine = riotAccount.tagLine)
    }

    override suspend fun getCurrentMatchUp(puuid: String, region: Region): MatchUpDomainModel {
        return when (val data = currentGameDataRepository.getCurrentGameData(puuid, region)) {
            is CurrentGame.InProgress -> processInProgress(data)
            CurrentGame.NotFound -> NoMatchUpFound
        }
    }

    private fun processInProgress(data: CurrentGame.InProgress): CurrentMatchUp {
        return domainMapper.map(data)
    }
}
