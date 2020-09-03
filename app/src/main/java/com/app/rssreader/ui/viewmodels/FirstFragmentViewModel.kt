package com.app.rssreader.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.app.rssreader.R
import com.app.rssreader.database.AppDatabase
import com.app.rssreader.database.Article
import com.app.rssreader.database.RSSChannel
import com.app.rssreader.database.RSSChannelDao
import com.prof.rssparser.Channel
import com.prof.rssparser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FirstFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var channelDao: RSSChannelDao

    private var _rssChannel = MutableLiveData<List<RSSChannel>>()
    val rssChannel: LiveData<List<RSSChannel>> = _rssChannel

    fun getChannels(){
        channelDao = Room.databaseBuilder(
            getApplication(),
            AppDatabase::class.java, getApplication<Application>().getString(R.string.database_rss_channel)
        ).allowMainThreadQueries().build().channelsDao()

        if(channelDao.getAll().isEmpty()) initDatabase(channelDao)
        _rssChannel.value = channelDao.getAll()
        cacheNews()
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

    private fun cacheNews(){
        for(channel: RSSChannel in channelDao.getAll()) {

            val articlesDao = Room.databaseBuilder(
                getApplication(),
                AppDatabase::class.java, channel.title!!
            ).allowMainThreadQueries().build().articlesDao()

            val parser = Parser.Builder()
                .context(getApplication())
                .cacheExpirationMillis(24L * 60L * 60L * 100L) // one day
                .build()

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val channel = parser.getChannel(channel.link)
                    articlesDao.deleteAll()
                    for (article: com.prof.rssparser.Article in channel.articles)
                        articlesDao.insert(
                            Article(
                                article.link!!,
                                article.description,
                                article.title,
                                article.image,
                                article.pubDate
                            )
                        )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}