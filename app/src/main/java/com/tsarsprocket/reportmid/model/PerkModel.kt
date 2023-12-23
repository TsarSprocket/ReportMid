package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.RIOTIconProvider

open class PerkModel(
    val id: Int,
    val name: String,
    val iconName: String,
    iconProvider: RIOTIconProvider
) {

    override fun equals(other: Any?): Boolean = if(other is RuneModel) id == other.id else false
    override fun hashCode() = id

    enum class STATMODS(
        val id: Int,
        val title: String,
        val iconPath: String,
    ) {
        HEALTH(5001, "здоровье",            "perk-images/StatMods/StatModsHealthScalingIcon.png"),
        ARMOR(5002, "броня",               "perk-images/StatMods/StatModsArmorIcon.png"),
        MAGRES(5003, "сопротивление магии", "perk-images/StatMods/StatModsMagicResIcon.png"),
        ATTSPD(5005, "скорость атаки",      "perk-images/StatMods/StatModsAttackSpeedIcon.png"),
        CDR(5007, "ускорение умений",    "perk-images/StatMods/StatModsCDRScalingIcon.png"),
        ADFORCE(5008, "адаптивная сила",     "perk-images/StatMods/StatModsAdaptiveForceIcon.png"),
    }

    companion object {
        fun getBasicPerks(iconProvider: RIOTIconProvider) = STATMODS.values().map { PerkModel(it.id, it.title, it.iconPath, iconProvider) }
    }
}