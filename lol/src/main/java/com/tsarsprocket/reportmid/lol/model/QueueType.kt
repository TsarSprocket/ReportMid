package com.tsarsprocket.reportmid.lol.model

enum class QueueType(val token: String) {
    Solo("RANKED_SOLO_5x5"),
    Flex("RANKED_TEAM_5x5"),
    Unknown("unknown");

    companion object {

        fun byName(name: String): QueueType = name.uppercase().let { capitalized -> entries.find { it.token == capitalized } } ?: Unknown
    }
}