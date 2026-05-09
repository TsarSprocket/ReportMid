package com.tsarsprocket.reportmid.matchUpView.impl.domain

import com.tsarsprocket.reportmid.currentGameData.api.data.CurrentGameDataRepository
import com.tsarsprocket.reportmid.currentGameData.api.data.model.CurrentGame
import com.tsarsprocket.reportmid.lol.api.domain.model.Region
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.MatchUpDomainModel
import com.tsarsprocket.reportmid.matchUpView.impl.domain.model.NoMatchUpFound
import javax.inject.Inject

internal class InteractorImpl @Inject constructor(
    private val currentGameDataRepository: CurrentGameDataRepository,
    private val domainMapper: DomainMapper,
) : Interactor {

    override suspend fun getCurrentMatchUp(puuid: String, region: Region): MatchUpDomainModel {
        return when(val data = currentGameDataRepository.getCurrentGameData(puuid, region)) {
            is CurrentGame.InProgress -> domainMapper.map(data)
            CurrentGame.NotFound -> NoMatchUpFound
        }
    }
}