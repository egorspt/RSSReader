package com.app.rssreader.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RSSChannel::class, Article::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun channelsDao(): RSSChannelDao
    abstract fun articlesDao(): ArticlesDao
}