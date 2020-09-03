package com.app.rssreader.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RSSChannel(
    @PrimaryKey val link: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "title") val title: String?
)