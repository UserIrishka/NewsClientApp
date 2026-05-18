package com.example.newsclientapp.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsclientapp.R
import com.example.newsclientapp.model.ArticleArticle
import coil.load

class NewsAdapter(private var articles: List<ArticleArticle>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.textTitle)
        val descriptionView: TextView = view.findViewById(R.id.textDescription)
        val dateView: TextView = view.findViewById(R.id.textDate)
        val newsImageView: ImageView = view.findViewById(R.id.newsImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.titleView.text = article.title
        holder.descriptionView.text = article.summary ?: "Описание отсутствует"

        val context = holder.itemView.context
        holder.dateView.text = context.getString(R.string.published_date, article.publishedAt.take(10))

        holder.newsImageView.load(article.imageUrl  ) {
            crossfade(true)
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.stat_notify_error)
        }
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = articles.size

    fun updateData(newArticles: List<ArticleArticle>) {
        this.articles = newArticles
        notifyItemRangeChanged(0, newArticles.size)
    }
}