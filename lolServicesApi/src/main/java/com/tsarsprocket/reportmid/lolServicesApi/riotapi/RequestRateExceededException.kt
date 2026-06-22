package com.tsarsprocket.reportmid.lolServicesApi.riotapi

import java.io.IOException
import kotlin.time.Duration

class RequestRateExceededException(val rate: Int, val interval: Duration) : IOException("Request rate of $rate/$interval is exceeded")