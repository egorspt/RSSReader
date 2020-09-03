package com.app.rssreader.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticlesDao {
    @Query("SELECT * FROM article")
    fun getAll(): List<Article>

    @Insert
    fun insert(article: Article)

    @Delete
    fun delete(article: Article)

    @Insert
    fun insertAll(article: List<Article>)
}