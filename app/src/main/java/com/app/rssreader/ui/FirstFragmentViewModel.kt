package com.app.rssreader.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.app.rssreader.R
import com.app.rssreader.database.AppDatabase
import com.app.rssreader.database.RSSChannel
import com.app.rssreader.database.RSSChannelDao

class FirstFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private var _rssChannel = MutableLiveData<List<RSSChannel>>()
    val rssChannel: LiveData<List<RSSChannel>> = _rssChannel

    fun getChannels(){
        val channelDao = Room.databaseBuilder(
            getApplication(),
            AppDatabase::class.java, getApplication<Application>().getString(R.string.database_rss_channel)
        ).allowMainThreadQueries().build().channelsDao()

        if(channelDao.getAll().isEmpty()) initDatabase(channelDao)
        _rssChannel.value = channelDao.getAll()
    }

    private fun initDatabase(channelDao: RSSChannelDao){
        channelDao.insert(RSSChannel("https://www.androidauthority.com/feed/",
            "Android News, Reviews, How To",
            "Android Authority"))
        channelDao.insert(
            RSSChannel("https://news.yandex.ru/auto.rss",
                "Первая в России служба автоматической обработки и систематизации новостей. Сообщения ведущих российских и мировых СМИ. Обновление в режиме реального времени 24 часа в сутки.",
                "Яндекс.Новости: Авто"))
        channelDao.insert(RSSChannel("http://lenta.ru/rss",
            "Новости, статьи, фотографии, видео. Семь дней в неделю, 24 часа в сутки.",
            "Lenta.ru : Новости"))
    }
}