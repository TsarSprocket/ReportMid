package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.common.Division

enum class DivisionModel( val shadowDivision: Division, val numeric: Int ) {
    V( Division.V, 5 ),
    IV( Division.IV, 4 ),
    III( Division.III, 3 ),
    II( Division.II, 2 ),
    I( Division.I, 1 )
}