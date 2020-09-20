package com.app.rssreader.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.rssreader.R
import com.app.rssreader.database.RSSChannel

class ChannelRVAdapter(private var list: List<RSSChannel>)
    : RecyclerView.Adapter<ChannelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ChannelViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        val channel: RSSChannel = list[position]
        holder.bind(channel)
    }

    override fun getItemCount(): Int = list.size

    fun setData(newList: List<RSSChannel>) {
        list = newList
        notifyDataSetChanged()
    }
}

class ChannelViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.channel_rv_item, parent, false)) {
    private var mTextView: TextView? = null
    private var mCardView: CardView? = null
    private var title: String? = null
    private var link: String? = null

    init {
        mTextView = itemView.findViewById(R.id.title)
        itemView.findViewById<CardView>(R.id.card_view)
            .setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundleOf("title" to title, "link" to link))
        }

    }

    fun bind(value: RSSChannel) {
        mTextView?.text = value.title
        title = value.title
        link = value.link
    }

}