package com.tsarsprocket.reportmid.summonerImpl.model

import com.tsarsprocket.reportmid.summonerApi.model.MyAccount
import com.tsarsprocket.reportmid.summonerRoom.MyAccountEntity

class MyAccountImpl(
    override val id: Long,
    override val summonerId: Long,
) : MyAccount {

    constructor(entity: MyAccountEntity) : this(entity.id, entity.summonerId)
}
