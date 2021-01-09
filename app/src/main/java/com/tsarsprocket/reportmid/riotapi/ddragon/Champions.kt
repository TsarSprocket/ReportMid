package com.tsarsprocket.reportmid.riotapi.ddragon

import com.google.gson.annotations.SerializedName

data class Champions(
    @SerializedName("data") val data: Map<String,Champion>,
    @SerializedName("format") val format: String,
    @SerializedName("type") val type: String,
    @SerializedName("version") val version: String,
)

data class Champion(
    @SerializedName("blurb") val blurb: String,
    @SerializedName("id") val id: String,
    @SerializedName("image") val image: Image,
    @SerializedName("info") val info: Info,
    @SerializedName("key") val key: Int,
    @SerializedName("name") val name: String,
    @SerializedName("partype") val partype: String,
    @SerializedName("stats") val stats: Stats,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("title") val title: String,
    @SerializedName("version") val version: String,
)

data class Image(
    @SerializedName("full") val full: String,
    @SerializedName("group") val group: String,
    @SerializedName("h") val h: Int,
    @SerializedName("sprite") val sprite: String,
    @SerializedName("w") val w: Int,
    @SerializedName("x") val x: Int,
    @SerializedName("y") val y: Int
)

data class Info(
    @SerializedName("attack") val attack: Int,
    @SerializedName("defense") val defense: Int,
    @SerializedName("difficulty") val difficulty: Int,
    @SerializedName("magic") val magic: Int
)

data class Stats(
    @SerializedName("armor") val armor: Double,
    @SerializedName("armorperlevel") val armorPerLevel: Double,
    @SerializedName("attackdamage") val attackDamage: Double,
    @SerializedName("attackdamageperlevel") val attackDamagePerLevel: Double,
    @SerializedName("attackrange") val attackRange: Double,
    @SerializedName("attackspeed") val attackSpeed: Double,
    @SerializedName("attackspeedperlevel") val attackSpeedPerLevel: Double,
    @SerializedName("crit") val crit: Double,
    @SerializedName("critperlevel") val critPerLevel: Double,
    @SerializedName("hp") val hp: Double,
    @SerializedName("hpperlevel") val hpPerLevel: Double,
    @SerializedName("hpregen") val hpRegen: Double,
    @SerializedName("hpregenperlevel") val hpRegenPerLevel: Double,
    @SerializedName("movespeed") val moveSpeed: Double,
    @SerializedName("mp") val mp: Double,
    @SerializedName("mpperlevel") val mpPerLevel: Double,
    @SerializedName("mpregen") val mpRegen: Double,
    @SerializedName("mpregenperlevel") val mpRegenPerLevel: Double,
    @SerializedName("spellblock") val spellBlock: Double,
    @SerializedName("spellblockperlevel") val spellBlockPerLevel: Double
)
