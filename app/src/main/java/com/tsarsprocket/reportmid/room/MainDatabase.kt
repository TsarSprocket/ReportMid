package com.tsarsprocket.reportmid.room

import androidx.room.Database
import com.tsarsprocket.reportmid.appApi.room.MainStorage
import com.tsarsprocket.reportmid.dataDragonRoom.ChampionEntity
import com.tsarsprocket.reportmid.dataDragonRoom.ItemEntity
import com.tsarsprocket.reportmid.dataDragonRoom.LanguageEntity
import com.tsarsprocket.reportmid.dataDragonRoom.RuneEntity
import com.tsarsprocket.reportmid.dataDragonRoom.RunePathEntity
import com.tsarsprocket.reportmid.dataDragonRoom.SummonerSpellEntity
import com.tsarsprocket.reportmid.dataDragonRoom.VersionEntity
import com.tsarsprocket.reportmid.stateRoom.CurrentAccountEntity
import com.tsarsprocket.reportmid.stateRoom.GlobalEntity
import com.tsarsprocket.reportmid.summonerRoom.FriendEntity
import com.tsarsprocket.reportmid.summonerRoom.MyAccountEntity
import com.tsarsprocket.reportmid.summonerRoom.SummonerEntity

@Database(
    entities = [
        GlobalEntity::class,
        CurrentAccountEntity::class,
        MyAccountEntity::class,
        FriendEntity::class,
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
abstract class MainDatabase : MainStorage()