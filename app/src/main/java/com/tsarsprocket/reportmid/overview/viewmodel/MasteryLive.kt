package com.tsarsprocket.reportmid.overview.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData

class MasteryLive {
    val shownLive = MutableLiveData( false )
    val iconLive = MutableLiveData<Drawable>( null )
//    val champNameLive = MutableLiveData<String>( null )
    val skillsLive = MutableLiveData<Skills>( null )

    data class Skills(val level: Int, val points: Int )
}