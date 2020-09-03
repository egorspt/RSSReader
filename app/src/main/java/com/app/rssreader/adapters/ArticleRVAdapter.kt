package com.app.rssreader.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.rssreader.R
import com.app.rssreader.database.Article

class ArticleRVAdapter(private val list: List<Article>)
    : RecyclerView.Adapter<ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArticleViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val channel: Article = list[position]
        holder.bind(channel)
    }

    override fun getItemCount(): Int = list.size
}

class ArticleViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.article_rv_item, parent, false)) {
    private var title: TextView? = null
    private var date: TextView? = null
    private var description: String? = null

    init {
        title = itemView.findViewById(R.id.title)
        itemView.findViewById<CardView>(R.id.card_view).setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundleOf("title" to title?.text.toString(), "description" to description))
        }

    }

    fun bind(value: Article) {
        title?.text = value.title
        description = value.description
        date?.text = value.date?.substring(0, value.date.length - 8)
    }

}