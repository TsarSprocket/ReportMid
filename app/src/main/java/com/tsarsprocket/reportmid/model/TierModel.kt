package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.common.Tier

enum class TierModel( val shadowTier: Tier, val shortName: String ) {
    UNRANKED( Tier.UNRANKED, "U" ),
    IRON( Tier.IRON, "I" ),
    BRONZE( Tier.BRONZE, "B" ),
    SILVER( Tier.SILVER, "S" ),
    GOLD( Tier.GOLD, "G" ),
    PLATINUM( Tier.PLATINUM, "P" ),
    DIAMOND( Tier.DIAMOND, "D" ),
    MASTER( Tier.MASTER, "M" ),
    GRANDMASTER( Tier.GRANDMASTER, "GM" ),
    CHALLENGER( Tier.CHALLENGER, "CH" )
}