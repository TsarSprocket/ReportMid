package com.tsarsprocket.reportmid.viewStateApi.viewIntent

interface CallViewIntent<ResultData> : ViewIntent {
    var returnIntentProducer: ReturnIntentFactory<ResultData>
    val removeRecentState: Boolean
}