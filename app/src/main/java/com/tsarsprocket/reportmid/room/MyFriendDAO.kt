package com.tsarsprocket.reportmid.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable

@Dao
interface MyFriendDAO {

    @Query( "SELECT * FROM my_friends" )
    fun getAll(): List<MyFriendEntity>

    @Query( "SELECT * FROM my_friends WHERE id = :id" )
    fun getById( id: Long ): MyFriendEntity

    @Query( "SELECT * FROM my_friends WHERE my_account_id = :accId" )
    fun getByAccountId( accId: Long ): List<MyFriendEntity>

    @Query( "SELECT * FROM my_friends WHERE my_account_id = :accId" )
    fun getByAccountIdObservable( accId: Long ): Observable<List<MyFriendEntity>>

    @Query( """
        SELECT s.*
        FROM summoners AS s
            JOIN my_friends AS mf ON ( s.id = mf.summoner_id )
        WHERE mf.my_account_id = :accId
    """ )
    fun getFriendsSummonersByAccId( accId: Long ): List<SummonerEntity>

    @Insert
    fun insert( myFriendEntity: MyFriendEntity ): Long

    @Delete
    fun delete( myFriendEntity: MyFriendEntity )
}
