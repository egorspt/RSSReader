package com.app.rssreader.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.rssreader.R
import com.app.rssreader.adapters.ArticleRVAdapter
import com.app.rssreader.adapters.ChannelRVAdapter
import com.app.rssreader.database.RSSChannel
import com.prof.rssparser.Parser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.nio.charset.Charset

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private lateinit var secondFragmentViewModel: SecondFragmentViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        secondFragmentViewModel = ViewModelProvider(this).get(SecondFragmentViewModel::class.java)
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rv).apply { layoutManager = LinearLayoutManager(activity) }

        secondFragmentViewModel.news.observe(viewLifecycleOwner, Observer {
            rv.adapter = ArticleRVAdapter(it)
        })
        secondFragmentViewModel.getNews(arguments?.getString("title")!!, arguments?.getString("link")!!)

    }
}