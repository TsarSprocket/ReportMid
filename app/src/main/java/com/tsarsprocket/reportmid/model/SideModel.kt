package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.common.Side

enum class SideModel( private val shadowSide: Side) {
    BLUE( Side.BLUE ),
    RED( Side.RED );
}