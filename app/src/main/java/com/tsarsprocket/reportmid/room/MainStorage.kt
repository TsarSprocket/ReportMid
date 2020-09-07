package com.tsarsprocket.reportmid.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        GlobalStateEntity::class,
        MyAccountEntity::class,
        MyFriendEntity::class,
        RegionEntity::class,
        SummonerEntity::class
    ], version = 1 )
abstract class MainStorage: RoomDatabase() {

    abstract fun globalStateDAO(): GlobalStateDAO

    abstract fun myAccountDAO(): MyAccountDAO

    abstract fun myFriendDAO(): MyFriendDAO

    abstract fun regionDAO(): RegionDAO

    abstract fun summonerDAO(): SummonerDAO
}