package com.tsarsprocket.reportmid.summonerRoom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FriendDAO {

    @Query("SELECT * FROM friends WHERE id = :id")
    fun getById(id: Long): FriendEntity

    @Query("SELECT * FROM friends WHERE my_account_id = :accId")
    fun getByAccountId(accId: Long): List<FriendEntity>

    @Insert
    fun insert(friendEntity: FriendEntity): Long

    @Delete
    fun delete(friendEntity: FriendEntity)
}
