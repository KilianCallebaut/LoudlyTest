package com.example.loudlytest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.loudlytest.adapters.RepoAdapter
import com.example.loudlytest.databinding.ActivityMainBinding
import com.example.loudlytest.requests.RepoRequestQueue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var count = 1
    private val repoAdapter = RepoAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.SearchButton.setOnClickListener {
            createRequest()
        }

        binding.apiContent.adapter = repoAdapter
        binding.apiContent.layoutManager = LinearLayoutManager(this)

        binding.apiContent.addOnScrollListener(
            object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1)) {
                        createRequest()
                    }
                }
            }

        )
    }

    private fun createRequest() {
        val url = "https://api.github.com/search/repositories?q=tetris&per_page=10&page=$count"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val jsonResponse = response.getJSONArray("items")
                repoAdapter.add_repos_last(jsonResponse)
                count += 1
            },
            Response.ErrorListener {error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            }
        )
        RepoRequestQueue.getInstance(this).addToRequestQueue(request)

    }
}