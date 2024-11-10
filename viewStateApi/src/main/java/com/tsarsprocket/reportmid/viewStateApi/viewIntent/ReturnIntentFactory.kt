package com.tsarsprocket.reportmid.viewStateApi.viewIntent

fun interface ReturnIntentFactory<in ResultData> {
    fun create(data: ResultData): ViewIntent
}