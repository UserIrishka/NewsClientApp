package com.example.newsclientapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsclientapp.adapter.NewsAdapter
import kotlinx.coroutines.launch
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressBar: ProgressBar
    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.newsRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        newsAdapter = NewsAdapter(emptyList())
        recyclerView.adapter = newsAdapter

        progressBar = findViewById(R.id.loadingProgressBar)
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is NewsUiState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is NewsUiState.Success -> {
                        progressBar.visibility = View.GONE
                        newsAdapter.updateData(state.news)
                    }
                    is NewsUiState.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {

                        viewModel.loadNextPage()
                    }
                }
            }
        })
    }
}