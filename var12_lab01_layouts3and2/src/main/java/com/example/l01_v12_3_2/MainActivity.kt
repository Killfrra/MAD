package com.example.l01_v12_3_2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        userName.text = sharedPref.getString("logged in as", "Anonymous")
        logoutButton.setOnClickListener {
            sharedPref.edit().putString("logged in as", "").apply()
            startActivity(
                Intent(this, LoginActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }

        val adapter = NewsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        swipeRefresh.isRefreshing = true
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
        }
        CoroutineScope(Dispatchers.Main).launch {
            adapter.submitList(
                NewsAPI.getInstance().getAllPosts()
            )
            swipeRefresh.isRefreshing = false
        }
    }
}

class NewsAdapter: ListAdapter<NewsAPI.PostSummary, NewsAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.postTitle)
        private val text: TextView = view.findViewById(R.id.postText)

        fun bind(summary: NewsAPI.PostSummary){
            title.text = summary.title
            text.text = summary.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsAPI.PostSummary>() {
            override fun areItemsTheSame(oldItem: NewsAPI.PostSummary, newItem: NewsAPI.PostSummary): Boolean =
                oldItem == newItem
            override fun areContentsTheSame(oldItem: NewsAPI.PostSummary, newItem: NewsAPI.PostSummary): Boolean =
                oldItem.num == newItem.num
        }
    }
}