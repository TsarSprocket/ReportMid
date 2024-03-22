package com.tsarsprocket.reportmid.state_api.model

import com.tsarsprocket.reportmid.lol.model.Region

data class CurrentAccount(
    val id: Long,
    var region: Region,
    var myAccountId: Long,
)
