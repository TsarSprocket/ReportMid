package com.tsarsprocket.reportmid.state_room

interface StateStoragePart {
    fun currentAccountDAO(): CurrentAccountDAO
    fun globalDAO(): GlobalDAO
}