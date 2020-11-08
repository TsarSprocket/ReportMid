package com.tsarsprocket.reportmid.presentation

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.tsarsprocket.reportmid.model.ChampionModel
import com.tsarsprocket.reportmid.model.SummonerModel

class PlayerPresentation() {
    val summoner = MutableLiveData<SummonerModel>()
    val championIconLive = MutableLiveData<Bitmap>()
    val summonerChampionSkillLive = MutableLiveData<Int>()
    val summonerNameLive = MutableLiveData<String>()
    val summonerLevelLive = MutableLiveData<Int>()
    val soloqueueRankLive = MutableLiveData<String>()
    val soloqueueWinrateLive = MutableLiveData<Float>()
    val summonerSpellDLive = MutableLiveData<Bitmap>()
    val summonerSpellFLive = MutableLiveData<Bitmap>()
    val primaryRunePathIconResIdLive = MutableLiveData<Int>()
    val secondaryRunePathIconResIdLive = MutableLiveData<Int>()
}