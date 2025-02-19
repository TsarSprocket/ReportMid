package com.tsarsprocket.reportmid.viewStateApi.viewIntent

import android.os.Parcelable

interface ReturnViewIntent<ResultData : Parcelable> : ViewIntent {
    var resultData: ResultData
}
