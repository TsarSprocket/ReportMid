package com.tsarsprocket.reportmid.summoner_impl.model

import com.tsarsprocket.reportmid.summoner_api.model.MyAccount
import com.tsarsprocket.reportmid.summoner_room.MyAccountEntity

class MyAccountImpl(
    override val id: Long,
    override val summonerId: Long,
) : MyAccount {

    constructor(entity: MyAccountEntity) : this(entity.id, entity.summonerId)
}
