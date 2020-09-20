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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val r = 4
    }
}