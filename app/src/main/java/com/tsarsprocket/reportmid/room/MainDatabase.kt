package com.tsarsprocket.reportmid.room

import androidx.room.Database
import com.tsarsprocket.reportmid.app_api.room.MainStorage
import com.tsarsprocket.reportmid.data_dragon_room.ChampionEntity
import com.tsarsprocket.reportmid.data_dragon_room.ItemEntity
import com.tsarsprocket.reportmid.data_dragon_room.LanguageEntity
import com.tsarsprocket.reportmid.data_dragon_room.RuneEntity
import com.tsarsprocket.reportmid.data_dragon_room.RunePathEntity
import com.tsarsprocket.reportmid.data_dragon_room.SummonerSpellEntity
import com.tsarsprocket.reportmid.data_dragon_room.VersionEntity
import com.tsarsprocket.reportmid.room.state.CurrentAccountDAO
import com.tsarsprocket.reportmid.room.state.CurrentAccountEntity
import com.tsarsprocket.reportmid.room.state.GlobalDAO
import com.tsarsprocket.reportmid.room.state.GlobalEntity

@Database(
    entities = [
        GlobalEntity::class,
        CurrentAccountEntity::class,
        MyAccountEntity::class,
        MyFriendEntity::class,
        RegionEntity::class,
        SummonerEntity::class,
        // Data Dragon
        VersionEntity::class,
        LanguageEntity::class,
        RunePathEntity::class,
        RuneEntity::class,
        ChampionEntity::class,
        SummonerSpellEntity::class,
        ItemEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class MainDatabase : MainStorage() {

    abstract fun globalDAO(): GlobalDAO

    abstract fun currentAccountDAO(): CurrentAccountDAO

    abstract fun myAccountDAO(): MyAccountDAO

    abstract fun myFriendDAO(): MyFriendDAO

    abstract fun regionDAO(): RegionDAO

    abstract fun summonerDAO(): SummonerDAO
}