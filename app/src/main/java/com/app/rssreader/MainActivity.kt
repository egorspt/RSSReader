package com.app.rssreader

import android.os.Build
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.app.rssreader.database.AppDatabase
import com.app.rssreader.database.RSSChannel
import com.app.rssreader.database.RSSChannelDao
import com.prof.rssparser.Parser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var channelDao: RSSChannelDao
    private var link: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Мои подписки"

        val parser = Parser.Builder()
            .context(this)
            .charset(Charset.forName("ISO-8859-7"))
            .cacheExpirationMillis(24L * 60L * 60L * 100L) // one day
            .build()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val editText = EditText(this)
            editText.setText(link)
            AlertDialog.Builder(this)
                    .setView(editText)
                    .setPositiveButton("Добавить") { dialogInterface, i ->
                        GlobalScope.launch {
                            try {
                                val channel = parser.getChannel(editText.text.toString())
                                channelDao.insert(RSSChannel(editText.text.toString(), channel.description, channel.title))
                            } catch (e: Exception) {
                                link = editText.text.toString()
                                dialogInterface.cancel()
                                AlertDialog.Builder(this@MainActivity)
                                    .setTitle("Ошибка в адресе")
                                    .setMessage("Подробности:\n" + e.message)
                            }
                        }
                    }
                    .setNegativeButton("Отмена") { dialogInterface, i ->
                        dialogInterface.cancel()
                    }.show()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initDatabase(){
        channelDao = Room.databaseBuilder(
            this,
            AppDatabase::class.java, getString(R.string.database_rss_channel)
        ).allowMainThreadQueries().build().channelsDao()
    }
}