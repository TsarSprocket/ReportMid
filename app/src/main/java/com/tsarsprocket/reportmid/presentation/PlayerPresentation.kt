package com.tsarsprocket.reportmid.presentation

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import com.tsarsprocket.reportmid.model.ChampionModel
import com.tsarsprocket.reportmid.model.SummonerModel

class PlayerPresentation() {
    val summoner = MutableLiveData<SummonerModel>()
    val championIconLive = MutableLiveData<Drawable>()
    val summonerChampionSkillLive = MutableLiveData<Int>()
    val summonerNameLive = MutableLiveData<String>()
    val summonerLevelLive = MutableLiveData<Int>()
    val soloqueueRankLive = MutableLiveData<String>()
    val soloqueueWinrateLive = MutableLiveData<Float>()
    val summonerSpellDLive = MutableLiveData<Bitmap>()
    val summonerSpellFLive = MutableLiveData<Bitmap>()
    val primaryRuneIconLive = MutableLiveData<Drawable>()
    val secondaryRunePathIconLive = MutableLiveData<Drawable>()
}