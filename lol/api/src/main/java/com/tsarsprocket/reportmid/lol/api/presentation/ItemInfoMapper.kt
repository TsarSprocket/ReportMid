package com.tsarsprocket.reportmid.lol.api.presentation

import com.tsarsprocket.reportmid.lol.api.domain.model.Item
import com.tsarsprocket.reportmid.lol.api.presentation.model.ItemInfo

interface ItemInfoMapper {
    fun map(item: Item): ItemInfo
    fun mapById(itemId: Int?): ItemInfo
}