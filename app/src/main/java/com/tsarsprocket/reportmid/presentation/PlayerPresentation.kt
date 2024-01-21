package com.tsarsprocket.reportmid.presentation

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import com.tsarsprocket.reportmid.summoner_api.model.SummonerModel

class PlayerPresentation() {
    val summoner = MutableLiveData<SummonerModel>()
    val championIconLive = MutableLiveData<Drawable>()
    val summonerChampionSkillLive = MutableLiveData<Int>()
    val summonerNameLive = MutableLiveData<String>()
    val summonerLevelLive = MutableLiveData<Long>()
    val soloqueueRankLive = MutableLiveData<String>()
    val soloqueueWinrateLive = MutableLiveData<Float>()
    val summonerSpellDLive = MutableLiveData<Drawable>()
    val summonerSpellFLive = MutableLiveData<Drawable>()
    val primaryRuneIconLive = MutableLiveData<Drawable>()
    val secondaryRunePathIconLive = MutableLiveData<Drawable>()
}