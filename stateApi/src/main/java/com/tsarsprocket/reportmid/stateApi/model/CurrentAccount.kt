package com.tsarsprocket.reportmid.stateApi.model

import com.tsarsprocket.reportmid.lol.api.model.Region

data class CurrentAccount(
    val id: Long,
    var region: Region,
    var myAccountId: Long,
)
