package dev.vkgo.pagination.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class RepoSearchResult(
    val data: LiveData<PagedList<Repo>>,
    val networkErrors: LiveData<String>
)