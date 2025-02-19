package com.tsarsprocket.reportmid.viewStateApi.viewIntent

import android.os.Parcelable

interface CallViewIntent<ResultData : Parcelable> : ViewIntent {
    val returnViewIntent: ReturnViewIntent<ResultData>
    val removeTopIntent: Boolean
}