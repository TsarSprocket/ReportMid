package com.tsarsprocket.reportmid.presentation

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData

class MasteryLive {
    val shownLive = MutableLiveData( false )
    val bitmapLive = MutableLiveData<Bitmap>( null )
//    val champNameLive = MutableLiveData<String>( null )
    val skillsLive = MutableLiveData<Skills>( null )

    data class Skills(val level: Int, val points: Int )
}