package com.tsarsprocket.reportmid.lol.api.presentation

import com.tsarsprocket.reportmid.lol.api.domain.model.SummonerSpell
import com.tsarsprocket.reportmid.lol.api.presentation.model.SummonerSpellInfo

interface SummonerSpellInfoMapper {
    fun map(summonerSpell: SummonerSpell): SummonerSpellInfo
    fun mapById(summonerSpellId: Long): SummonerSpellInfo
}