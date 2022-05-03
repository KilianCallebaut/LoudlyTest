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

        // Create the adapter and layoutmanager necessary for the recyclerview
        binding.apiContent.adapter = repoAdapter
        binding.apiContent.layoutManager = LinearLayoutManager(this)

        // Add onScrollListener function checking if the screen is scrolled to the bottom
        binding.apiContent.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
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
        // Create the getter request for the api call

        // Get the query value or use the default 'tetris'
        val query = if (binding.searchField.text.toString()
                .isEmpty()
        ) "tetris" else binding.searchField.text.toString()
        val url = "https://api.github.com/search/repositories?q=$query&per_page=10&page=$count"

        // Make the api request fetching the JSON object
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val jsonResponse = response.getJSONArray("items")
                repoAdapter.add_repos_last(jsonResponse)
                count += 1
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            }
        )
        RepoRequestQueue.getInstance(this).addToRequestQueue(request)

    }
}