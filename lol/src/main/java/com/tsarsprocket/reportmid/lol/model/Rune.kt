package com.tsarsprocket.reportmid.lol.model

abstract class Rune(
    id: Int,
    val key: String,
    name: String,
    val slotNo: Int,
    iconPath: String,
) : Perk(id, name, iconPath) {

    abstract val runePath: RunePath
}
