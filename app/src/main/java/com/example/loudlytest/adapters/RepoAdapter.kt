package com.example.loudlytest.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loudlytest.R
import com.example.loudlytest.objects.RepoItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.repo_item.view.*
import org.json.JSONArray

class RepoAdapter(private val repoItems: MutableList<RepoItem>) :
    RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.repo_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        // Bind view for every repo entry
        val curRepo = repoItems[position]
        holder.itemView.apply {
            repoName.text = curRepo.repoName
            loginOwner.text = curRepo.loginOwner
            size.text = curRepo.size
        }
        // Change background color to yellow if the repo has a wiki
        if (curRepo.has_wiki) {
            holder.itemView.repoItemLayout.setBackgroundColor(Color.parseColor("#FFC107"))
        } else {
            holder.itemView.repoItemLayout.setBackgroundColor(Color.WHITE)

        }
    }

    override fun getItemCount(): Int {
        return repoItems.size
    }


    fun add_repos_last(data: JSONArray) {
        // append JSON data to the end of the list
        val index = repoItems.size
        for (i in 0 until data.length()) {
            val repoObject = data.getJSONObject(i)
            repoItems.add(
                RepoItem(
                    repoObject.getString("name"),
                    repoObject.getJSONObject("owner").getString("login"),
                    repoObject.getInt("size").toString(),
                    repoObject.getBoolean("has_wiki")
                )
            )
        }
        // update the observer list on screen
        notifyItemRangeChanged(index, data.length())
    }

}