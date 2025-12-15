package com.tsarsprocket.reportmid.lol.api.domain.model

data class KnownItem(
    override val riotId: Int,
    val name: String,
    val imageName: String,
) : Item