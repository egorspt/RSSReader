package com.app.rssreader.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RSSChannelDao {
    @Query("SELECT * FROM rssChannel")
    fun getAll(): List<RSSChannel>

    @Insert
    fun insert(rssChannel: RSSChannel)

    @Delete
    fun delete(rssChannel: RSSChannel)
}