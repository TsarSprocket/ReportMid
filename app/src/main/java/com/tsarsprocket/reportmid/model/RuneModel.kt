package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.RIOTIconProvider

const val RES_NAME_PREFIX_RUNE= "rune"

abstract class RuneModel(
    id: Int,
    val key: String,
    name: String,
    val slotNo: Int,
    iconPath: String,
    iconProvider: RIOTIconProvider,
) : PerkModel(id, name, iconPath, iconProvider) {

    abstract val runePath: RunePathModel
}
