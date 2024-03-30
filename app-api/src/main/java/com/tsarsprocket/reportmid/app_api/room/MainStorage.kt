package com.tsarsprocket.reportmid.app_api.room

import androidx.room.RoomDatabase
import com.tsarsprocket.reportmid.data_dragon_room.DataDragonStoragePart
import com.tsarsprocket.reportmid.state_room.StateStoragePart
import com.tsarsprocket.reportmid.summoner_room.MyAccountStoragePart
import com.tsarsprocket.reportmid.summoner_room.SummonerStoragePart

abstract class MainStorage :
    RoomDatabase(),
    DataDragonStoragePart,
    SummonerStoragePart,
    MyAccountStoragePart,
    StateStoragePart