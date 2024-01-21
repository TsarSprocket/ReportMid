package com.tsarsprocket.reportmid.app_api.room

import androidx.room.RoomDatabase
import com.tsarsprocket.reportmid.data_dragon_room.DataDragonStoragePart
import com.tsarsprocket.reportmid.lol_room.room.RegionStoragePart
import com.tsarsprocket.reportmid.my_account_room.MyAccountStoragePart
import com.tsarsprocket.reportmid.summoner_room.SummonerStoragePart

abstract class MainStorage :
    RoomDatabase(),
    DataDragonStoragePart,
    RegionStoragePart,
    SummonerStoragePart,
    MyAccountStoragePart