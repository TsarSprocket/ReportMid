package com.tsarsprocket.reportmid.viewStateApi.viewIntent

import android.os.Parcelable

interface ReturnIntentFactory<in ResultData> : Parcelable {
    fun create(data: ResultData): ViewIntent
}