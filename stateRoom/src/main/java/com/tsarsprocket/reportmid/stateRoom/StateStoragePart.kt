package com.tsarsprocket.reportmid.stateRoom

interface StateStoragePart {
    fun currentAccountDAO(): CurrentAccountDAO
    fun globalDAO(): GlobalDAO
}