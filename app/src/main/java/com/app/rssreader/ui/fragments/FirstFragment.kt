package com.app.rssreader.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.app.rssreader.R
import com.app.rssreader.adapters.ChannelRVAdapter
import com.app.rssreader.database.AppDatabase
import com.app.rssreader.database.RSSChannel
import com.app.rssreader.database.RSSChannelDao
import com.app.rssreader.ui.viewmodels.FirstFragmentViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prof.rssparser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import java.nio.charset.Charset

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var firstFragmentViewModel: FirstFragmentViewModel
    private lateinit var channelDao: RSSChannelDao
    private lateinit var parser: Parser
    private var link = ""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Подписки"
        firstFragmentViewModel = ViewModelProvider(this).get(FirstFragmentViewModel::class.java)
        initDatabase()
        initRSSParser()
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rv).apply { layoutManager = LinearLayoutManager(activity) }

        firstFragmentViewModel.rssChannel.observe(viewLifecycleOwner, Observer {
            rv.adapter = ChannelRVAdapter(it)
        })
        firstFragmentViewModel.getChannels()

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val editText = EditText(activity)
            editText.setText(link)
            AlertDialog.Builder(requireContext())
                .setTitle("Добавить канал")
                .setView(editText)
                .setPositiveButton("Добавить") { dialogInterface, i ->
                    GlobalScope.launch(Dispatchers.Main) {
                        try {
                            val channel = parser.getChannel(editText.text.toString())
                            channelDao.insert(RSSChannel(editText.text.toString(), channel.description, channel.title))
                            (rv.adapter as ChannelRVAdapter).setData(channelDao.getAll())
                            rv.adapter?.notifyDataSetChanged()
                        } catch (e: Exception) {
                            link = editText.text.toString()
                            dialogInterface.cancel()
                            AlertDialog.Builder(requireContext())
                                .setTitle("Ошибка в адресе")
                                .setMessage("Подробности:\n" + e.message)
                                .show()
                        }
                    }
                }
                .setNegativeButton("Отмена") { dialogInterface, i ->
                    dialogInterface.cancel()
                }.show()

        }
    }

    private fun initDatabase(){
        channelDao = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, getString(R.string.database_rss_channel)
        ).allowMainThreadQueries().build().channelsDao()
    }

    private fun initRSSParser(){
        parser = Parser.Builder()
        .context(requireContext())
        .cacheExpirationMillis(24L * 60L * 60L * 100L) // one day
        .build()
    }
}