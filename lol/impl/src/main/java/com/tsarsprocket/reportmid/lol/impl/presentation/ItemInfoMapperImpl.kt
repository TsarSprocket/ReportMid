package com.tsarsprocket.reportmid.lol.impl.presentation

import com.tsarsprocket.reportmid.dataDragonApi.data.DataDragon
import com.tsarsprocket.reportmid.lol.api.domain.model.ITEM_ID_EMPTY
import com.tsarsprocket.reportmid.lol.api.domain.model.Item
import com.tsarsprocket.reportmid.lol.api.domain.model.KnownItem
import com.tsarsprocket.reportmid.lol.api.domain.model.UnknownItem
import com.tsarsprocket.reportmid.lol.api.presentation.ItemInfoMapper
import com.tsarsprocket.reportmid.lol.api.presentation.model.ItemInfo
import com.tsarsprocket.reportmid.utils.common.logError
import javax.inject.Inject

internal class ItemInfoMapperImpl @Inject constructor(
    private val dataDragon: DataDragon,
) : ItemInfoMapper {

    override fun map(item: Item?): ItemInfo {
        return when(item) {
            is KnownItem -> ItemInfo.Known(
                icon = dataDragon.tail.getItemImageUrl(item),
                name = item.name
            )

            is UnknownItem -> ItemInfo.Unknown
            else -> ItemInfo.Empty
        }
    }

    override fun mapById(itemId: Int?): ItemInfo {
        return if(itemId != null && itemId != ITEM_ID_EMPTY) {
            try {
                map(dataDragon.tail.getItemById(itemId))
            } catch(e: Exception) {
                logError("Cannot find item with id=$itemId", e)
                ItemInfo.Unknown
            }
        } else {
            ItemInfo.Empty
        }
    }
}