package com.tsarsprocket.reportmid.riotapi.matchV4

import com.google.gson.annotations.SerializedName

data class ParticipantStatsDto(
    @SerializedName("item0") val item0: Long,
    @SerializedName("item2") val item2: Long,
    @SerializedName("totalUnitsHealed") val totalUnitsHealed: Int,
    @SerializedName("item1") val item1: Long,
    @SerializedName("largestMultiKill") val largestMultiKill: Int,
    @SerializedName("goldEarned") val goldEarned: Int,
    @SerializedName("firstInhibitorKill") val firstInhibitorKill: Boolean,
    @SerializedName("physicalDamageTaken") val physicalDamageTaken: Long,
    @SerializedName("nodeNeutralizeAssist") val nodeNeutralizeAssist: Int?,
    @SerializedName("totalPlayerScore") val totalPlayerScore: Int,
    @SerializedName("champLevel") val champLevel: Int,
    @SerializedName("damageDealtToObjectives") val damageDealtToObjectives: Long,
    @SerializedName("totalDamageTaken") val totalDamageTaken: Long,
    @SerializedName("neutralMinionsKilled") val neutralMinionsKilled: Int,
    @SerializedName("deaths") val deaths: Int,
    @SerializedName("tripleKills") val tripleKills: Int,
    @SerializedName("magicDamageDealtToChampions") val magicDamageDealtToChampions: Long,
    @SerializedName("wardsKilled") val wardsKilled: Int,
    @SerializedName("pentaKills") val pentaKills: Int,
    @SerializedName("damageSelfMitigated") val damageSelfMitigated: Long,
    @SerializedName("largestCriticalStrike") val largestCriticalStrike: Int,
    @SerializedName("nodeNeutralize") val nodeNeutralize: Int?,
    @SerializedName("totalTimeCrowdControlDealt") val totalTimeCrowdControlDealt: Int,
    @SerializedName("firstTowerKill") val firstTowerKill: Boolean,
    @SerializedName("magicDamageDealt") val magicDamageDealt: Long,
    @SerializedName("totalScoreRank") val totalScoreRank: Int,
    @SerializedName("nodeCapture") val nodeCapture: Int?,
    @SerializedName("wardsPlaced") val wardsPlaced: Int,
    @SerializedName("totalDamageDealt") val totalDamageDealt: Long,
    @SerializedName("timeCCingOthers") val timeCCingOthers: Long,
    @SerializedName("magicalDamageTaken") val magicalDamageTaken: Long,
    @SerializedName("largestKillingSpree") val largestKillingSpree: Int,
    @SerializedName("totalDamageDealtToChampions") val totalDamageDealtToChampions: Long,
    @SerializedName("physicalDamageDealtToChampions") val physicalDamageDealtToChampions: Long,
    @SerializedName("neutralMinionsKilledTeamJungle") val neutralMinionsKilledTeamJungle: Int,
    @SerializedName("totalMinionsKilled") val totalMinionsKilled: Int,
    @SerializedName("firstInhibitorAssist") val firstInhibitorAssist: Boolean,
    @SerializedName("visionWardsBoughtInGame") val visionWardsBoughtInGame: Int,
    @SerializedName("objectivePlayerScore") val objectivePlayerScore: Int,
    @SerializedName("kills") val kills: Int,
    @SerializedName("firstTowerAssist") val firstTowerAssist: Boolean,
    @SerializedName("combatPlayerScore") val combatPlayerScore: Int,
    @SerializedName("inhibitorKills") val inhibitorKills: Int,
    @SerializedName("turretKills") val turretKills: Int,
    @SerializedName("participantId") val participantId: Int,
    @SerializedName("trueDamageTaken") val trueDamageTaken: Long,
    @SerializedName("firstBloodAssist") val firstBloodAssist: Boolean,
    @SerializedName("nodeCaptureAssist") val nodeCaptureAssist: Int?,
    @SerializedName("assists") val assists: Int,
    @SerializedName("teamObjective") val teamObjective: Int?,
    @SerializedName("altarsNeutralized") val altarsNeutralized: Int?,
    @SerializedName("goldSpent") val goldSpent: Int,
    @SerializedName("damageDealtToTurrets") val damageDealtToTurrets: Long,
    @SerializedName("altarsCaptured") val altarsCaptured: Int?,
    @SerializedName("win") val win: Boolean,
    @SerializedName("totalHeal") val totalHeal: Long,
    @SerializedName("unrealKills") val unrealKills: Int,
    @SerializedName("visionScore") val visionScore: Long,
    @SerializedName("physicalDamageDealt") val physicalDamageDealt: Long,
    @SerializedName("firstBloodKill") val firstBloodKill: Boolean,
    @SerializedName("longestTimeSpentLiving") val longestTimeSpentLiving: Int,
    @SerializedName("killingSprees") val killingSprees: Int,
    @SerializedName("sightWardsBoughtInGame") val sightWardsBoughtInGame: Int,
    @SerializedName("trueDamageDealtToChampions") val trueDamageDealtToChampions: Long,
    @SerializedName("neutralMinionsKilledEnemyJungle") val neutralMinionsKilledEnemyJungle: Int,
    @SerializedName("doubleKills") val doubleKills: Int,
    @SerializedName("trueDamageDealt") val trueDamageDealt: Long,
    @SerializedName("quadraKills") val quadraKills: Int,
    @SerializedName("item4") val item4: Long,
    @SerializedName("item3") val item3: Long,
    @SerializedName("item6") val item6: Long,
    @SerializedName("item5") val item5: Long,
    @SerializedName("playerScore0") val playerScore0: Int,
    @SerializedName("playerScore1") val playerScore1: Int,
    @SerializedName("playerScore2") val playerScore2: Int,
    @SerializedName("playerScore3") val playerScore3: Int,
    @SerializedName("playerScore4") val playerScore4: Int,
    @SerializedName("playerScore5") val playerScore5: Int,
    @SerializedName("playerScore6") val playerScore6: Int,
    @SerializedName("playerScore7") val playerScore7: Int,
    @SerializedName("playerScore8") val playerScore8: Int,
    @SerializedName("playerScore9") val playerScore9: Int,
    @SerializedName("perk0") val perk0: Int,
    @SerializedName("perk0Var1") val perk0Var1: Int,
    @SerializedName("perk0Var2") val perk0Var2: Int,
    @SerializedName("perk0Var3") val perk0Var3: Int,
    @SerializedName("perk1") val perk1: Int,
    @SerializedName("perk1Var1") val perk1Var1: Int,
    @SerializedName("perk1Var2") val perk1Var2: Int,
    @SerializedName("perk1Var3") val perk1Var3: Int,
    @SerializedName("perk2") val perk2: Int,
    @SerializedName("perk2Var1") val perk2Var1: Int,
    @SerializedName("perk2Var2") val perk2Var2: Int,
    @SerializedName("perk2Var3") val perk2Var3: Int,
    @SerializedName("perk3") val perk3: Int,
    @SerializedName("perk3Var1") val perk3Var1: Int,
    @SerializedName("perk3Var2") val perk3Var2: Int,
    @SerializedName("perk3Var3") val perk3Var3: Int,
    @SerializedName("perk4") val perk4: Int,
    @SerializedName("perk4Var1") val perk4Var1: Int,
    @SerializedName("perk4Var2") val perk4Var2: Int,
    @SerializedName("perk4Var3") val perk4Var3: Int,
    @SerializedName("perk5") val perk5: Int,
    @SerializedName("perk5Var1") val perk5Var1: Int,
    @SerializedName("perk5Var2") val perk5Var2: Int,
    @SerializedName("perk5Var3") val perk5Var3: Int,
    @SerializedName("perkPrimaryStyle") val perkPrimaryStyle: Int,
    @SerializedName("perkSubStyle") val perkSubStyle: Int,
    @SerializedName("statPerk0") val statPerk0: Int,
    @SerializedName("statPerk1") val statPerk1: Int,
    @SerializedName("statPerk2") val statPerk2: Int,
)