package com.tsarsprocket.reportmid.presentation

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import com.tsarsprocket.reportmid.summonerApi.model.Summoner

class PlayerPresentation() {
    val summoner = MutableLiveData<Summoner>()
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