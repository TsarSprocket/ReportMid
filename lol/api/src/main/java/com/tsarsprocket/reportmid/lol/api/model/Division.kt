package com.tsarsprocket.reportmid.lol.api.model

enum class Division(val roman: String, val numeric: Int) {
    I(roman = "I", numeric = 1),
    II(roman = "II", numeric = 2),
    III(roman = "III", numeric = 3),
    IV(roman = "IV", numeric = 4),
    V(roman = "V", numeric = 5),
    UNKNOWN(roman = "unknown", numeric = -1);

    companion object {

        @OptIn(ExperimentalStdlibApi::class)
        fun byRoman(roman: String) = roman.uppercase().let { capitalized -> entries.find { it.roman == capitalized } } ?: UNKNOWN
    }
}