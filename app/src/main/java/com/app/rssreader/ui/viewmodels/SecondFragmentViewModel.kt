package com.app.rssreader.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.app.rssreader.database.AppDatabase
import com.app.rssreader.database.Article
import com.app.rssreader.database.ArticlesDao
import com.prof.rssparser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SecondFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var articlesDao: ArticlesDao

    private var _news = MutableLiveData<List<Article>>()
    val news: LiveData<List<Article>> = _news

    fun getNews(channelTitle: String, channelLink: String){
        articlesDao = Room.databaseBuilder(
            getApplication(),
            AppDatabase::class.java, channelTitle
        ).allowMainThreadQueries().build().articlesDao()

        _news.value = articlesDao.getAll()
        refreshNews(channelLink)
    }

    private fun refreshNews(link: String){
        val parser = Parser.Builder()
            .context(getApplication())
            .cacheExpirationMillis(24L * 60L * 60L * 100L) // one day
            .build()

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val channel = parser.getChannel(link)
                articlesDao.deleteAll()
                for(article: com.prof.rssparser.Article in channel.articles)
                    articlesDao.insert(Article(article.link!!, article.description, article.title, article.image, article.pubDate))
                _news.postValue(articlesDao.getAll())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}