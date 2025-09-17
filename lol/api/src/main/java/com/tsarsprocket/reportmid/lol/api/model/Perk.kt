package com.tsarsprocket.reportmid.lol.api.model

import com.tsarsprocket.reportmid.utils.common.orFalse

open class Perk(
    val id: Int,
    val name: String,
    val iconName: String
) {

    override fun equals(other: Any?): Boolean = (other as? Perk)?.let { id == it.id }.orFalse()

    override fun hashCode() = id

    enum class STATMODS(
        val id: Int,
        val title: String,
        val iconPath: String,
    ) {
        HEALTH(5001, "здоровье", "perk-images/StatMods/StatModsHealthScalingIcon.png"),
        ARMOR(5002, "броня", "perk-images/StatMods/StatModsArmorIcon.png"),
        MAGRES(5003, "сопротивление магии", "perk-images/StatMods/StatModsMagicResIcon.png"),
        ATTSPD(5005, "скорость атаки", "perk-images/StatMods/StatModsAttackSpeedIcon.png"),
        CDR(5007, "ускорение умений", "perk-images/StatMods/StatModsCDRScalingIcon.png"),
        ADFORCE(5008, "адаптивная сила", "perk-images/StatMods/StatModsAdaptiveForceIcon.png"),
        MOVE_SPEED(5010, "скорость передвижения", "perk-images/StatMods/StatModsMovementSpeedIcon.png"),
        HEALTH_PLUS(5011, "рост здоровья", "perk-images/StatMods/StatModsHealthPlusIcon.png"),
        TENANCY(5013, "устойчивость", "perk-images/StatMods/StatModsTenacityIcon.png"),
    }

    companion object {
        fun getBasicPerks() = STATMODS.entries.map { Perk(it.id, it.title, it.iconPath) }
    }
}