package com.tsarsprocket.reportmid.model

enum class RegionModel(
    val countryCode: String,
    val title: String,
    val shadowRegion: com.merakianalytics.orianna.types.common.Region
) {
    BRAZIL( com.merakianalytics.orianna.types.common.Region.BRAZIL.tag, "Brazil", com.merakianalytics.orianna.types.common.Region.BRAZIL ),
    EUROPE_NORTH_EAST( com.merakianalytics.orianna.types.common.Region.EUROPE_NORTH_EAST.tag, "Europe North East", com.merakianalytics.orianna.types.common.Region.EUROPE_NORTH_EAST ),
    EUROPE_WEST( com.merakianalytics.orianna.types.common.Region.EUROPE_WEST.tag, "Europe West", com.merakianalytics.orianna.types.common.Region.EUROPE_WEST ),
    JAPAN( com.merakianalytics.orianna.types.common.Region.JAPAN.tag, "Japan", com.merakianalytics.orianna.types.common.Region.JAPAN ),
    KOREA( com.merakianalytics.orianna.types.common.Region.KOREA.tag, "Korea", com.merakianalytics.orianna.types.common.Region.KOREA ),
    LATIN_AMERICA_NORTH( com.merakianalytics.orianna.types.common.Region.LATIN_AMERICA_NORTH.tag, "Latin America North", com.merakianalytics.orianna.types.common.Region.LATIN_AMERICA_NORTH ),
    LATIN_AMERICA_SOUTH( com.merakianalytics.orianna.types.common.Region.LATIN_AMERICA_SOUTH.tag, "Latin America South", com.merakianalytics.orianna.types.common.Region.LATIN_AMERICA_SOUTH ),
    NORTH_AMERICA( com.merakianalytics.orianna.types.common.Region.NORTH_AMERICA.tag, "North America", com.merakianalytics.orianna.types.common.Region.NORTH_AMERICA ),
    OCEANIA( com.merakianalytics.orianna.types.common.Region.OCEANIA.tag, "Oceania", com.merakianalytics.orianna.types.common.Region.OCEANIA ),
    RUSSIA( com.merakianalytics.orianna.types.common.Region.RUSSIA.tag, "Russia", com.merakianalytics.orianna.types.common.Region.RUSSIA ),
    TURKEY( com.merakianalytics.orianna.types.common.Region.TURKEY.tag, "Turkey", com.merakianalytics.orianna.types.common.Region.TURKEY );
}