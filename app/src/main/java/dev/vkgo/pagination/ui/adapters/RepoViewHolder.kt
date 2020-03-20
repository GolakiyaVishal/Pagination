package dev.vkgo.pagination.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.vkgo.pagination.R
import dev.vkgo.pagination.model.Repo

class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.repo_name)
    private val description: TextView = itemView.findViewById(R.id.repo_description)
    private val stars: TextView = itemView.findViewById(R.id.repo_stars)
    private val language: TextView = itemView.findViewById(R.id.repo_language)
    private val forks: TextView = itemView.findViewById(R.id.repo_forks)

    private var repo: Repo? = null

    init {
        itemView.setOnClickListener {
            repo?.url?.let {
                itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
            }
        }
    }

    fun bind(repo: Repo?) {
        if (repo == null) {
            val resource = itemView.resources
            name.text = resource.getString(R.string.loading)
            description.visibility = View.GONE
            language.visibility = View.GONE
            stars.text = resource.getString(R.string.unknown)
            forks.text = resource.getString(R.string.unknown)

        } else {
            setRepoData(repo)
        }
    }

    private fun setRepoData(repo: Repo) {
        this.repo = repo
        name.text = repo.name

        var descriptionVisibility = View.GONE
        if (repo.description != null) {
            description.text = repo.description
            descriptionVisibility = View.VISIBLE
        }
        description.visibility = descriptionVisibility

        var languageVisibility = View.GONE
        if (repo.language != null) {
            language.text = repo.language
            languageVisibility = View.VISIBLE
        }
        language.visibility = languageVisibility

        stars.text = repo.stars.toString()
        forks.text = repo.forks.toString()
    }

    companion object {
        fun create(parent: ViewGroup): RepoViewHolder {
            return RepoViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.repo_view_item, parent, false
                )
            )
        }
    }
}