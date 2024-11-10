package com.tsarsprocket.reportmid.appApi.room

import androidx.room.RoomDatabase
import com.tsarsprocket.reportmid.dataDragonRoom.DataDragonStoragePart
import com.tsarsprocket.reportmid.stateRoom.StateStoragePart
import com.tsarsprocket.reportmid.summonerRoom.FriendStoragePart
import com.tsarsprocket.reportmid.summonerRoom.MyAccountStoragePart
import com.tsarsprocket.reportmid.summonerRoom.SummonerStoragePart

abstract class MainStorage :
    RoomDatabase(),
    DataDragonStoragePart,
    SummonerStoragePart,
    MyAccountStoragePart,
    StateStoragePart,
    FriendStoragePart