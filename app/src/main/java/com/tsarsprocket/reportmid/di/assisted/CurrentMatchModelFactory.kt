package com.tsarsprocket.reportmid.di.assisted

import com.tsarsprocket.reportmid.model.CurrentMatchModel
import com.tsarsprocket.reportmid.summoner_api.model.Summoner
import dagger.assisted.AssistedFactory

@AssistedFactory
interface CurrentMatchModelFactory {
    fun create(summoner: Summoner): CurrentMatchModel
}