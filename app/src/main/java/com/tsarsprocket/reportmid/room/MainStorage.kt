package com.tsarsprocket.reportmid.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tsarsprocket.reportmid.room.ddragon.ChampionDao
import com.tsarsprocket.reportmid.room.ddragon.ChampionEntity
import com.tsarsprocket.reportmid.room.ddragon.ItemDao
import com.tsarsprocket.reportmid.room.ddragon.ItemEntity
import com.tsarsprocket.reportmid.room.ddragon.LanguageDao
import com.tsarsprocket.reportmid.room.ddragon.LanguageEntity
import com.tsarsprocket.reportmid.room.ddragon.RuneDao
import com.tsarsprocket.reportmid.room.ddragon.RuneEntity
import com.tsarsprocket.reportmid.room.ddragon.RunePathDao
import com.tsarsprocket.reportmid.room.ddragon.RunePathEntity
import com.tsarsprocket.reportmid.room.ddragon.SummonerSpellDao
import com.tsarsprocket.reportmid.room.ddragon.SummonerSpellEntity
import com.tsarsprocket.reportmid.room.ddragon.VersionDao
import com.tsarsprocket.reportmid.room.ddragon.VersionEntity
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
abstract class MainStorage: RoomDatabase() {

    abstract fun globalDAO(): GlobalDAO

    abstract fun currentAccountDAO(): CurrentAccountDAO

    abstract fun myAccountDAO(): MyAccountDAO

    abstract fun myFriendDAO(): MyFriendDAO

    abstract fun regionDAO(): RegionDAO

    abstract fun summonerDAO(): SummonerDAO

    // Data Dragon
    abstract fun ddragonVersionDao(): VersionDao

    abstract fun ddragonLanguageDao(): LanguageDao

    abstract fun runePathDao(): RunePathDao

    abstract fun runeDao(): RuneDao

    abstract fun championDao(): ChampionDao

    abstract fun summonerSpellDao(): SummonerSpellDao

    abstract fun itemDao(): ItemDao
}