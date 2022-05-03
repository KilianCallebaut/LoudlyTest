package com.example.loudlytest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.loudlytest.databinding.ActivityMainBinding
import com.example.loudlytest.requests.RepoRequestQueue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.SearchButton.setOnClickListener {
            createRequest(binding.searchField)
        }
    }

    private fun createRequest(view: View) {
        val url = "https://api.github.com/search/repositories?q=tetris&per_page=10"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val jsonResponse = response.getJSONArray("items")
                Toast.makeText(this.applicationContext, jsonResponse.length().toString(), Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener {error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            }
        )
        RepoRequestQueue.getInstance(this).addToRequestQueue(request)
    }
}