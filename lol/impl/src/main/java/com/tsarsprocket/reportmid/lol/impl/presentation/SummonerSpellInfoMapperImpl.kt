package com.tsarsprocket.reportmid.lol.impl.presentation

import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.api.domain.model.SummonerSpell
import com.tsarsprocket.reportmid.lol.api.presentation.SummonerSpellInfoMapper
import com.tsarsprocket.reportmid.lol.api.presentation.model.SummonerSpellInfo
import javax.inject.Inject

class SummonerSpellInfoMapperImpl @Inject constructor(
    private val dataDragon: DataDragon,
) : SummonerSpellInfoMapper {

    override fun map(summonerSpell: SummonerSpell): SummonerSpellInfo = SummonerSpellInfo(
        iconUrl = dataDragon.tail.getSummonerSpellImageUrl(summonerSpell.iconName),
        name = summonerSpell.name,
    )

    override fun mapById(summonerSpellId: Long): SummonerSpellInfo = map(dataDragon.tail.getSummonerSpellById(summonerSpellId))
}