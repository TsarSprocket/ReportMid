package com.tsarsprocket.reportmid.viewStateImpl.backstack

import com.tsarsprocket.reportmid.viewStateApi.viewIntent.ViewIntent
import java.util.UUID

class BackOperation(
    val goBackIntent: ViewIntent,
) {
    val uuid = UUID.randomUUID()
}
