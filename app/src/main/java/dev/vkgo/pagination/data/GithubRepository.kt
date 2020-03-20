package dev.vkgo.pagination.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dev.vkgo.pagination.api.GithubService
import dev.vkgo.pagination.api.RepoSearchResponse
import dev.vkgo.pagination.api.searchRepos
import dev.vkgo.pagination.model.RepoSearchResult
import dev.vkgo.pagination.roomdb.GithubLocalCache

class GithubRepository(
    private val service: GithubService,
    private val cache: GithubLocalCache
) {

    fun search(query: String): RepoSearchResult {
//        lastRequestedPage = 1
//        requestAndSaveData(query)
//        val data = cache.reposByName(query)

        val dataSourceFactory = cache.reposByName(query)

        val boundaryCallback = RepoBoundaryCallback(query, service, cache)
        val networkError = boundaryCallback.networkError

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()
        return RepoSearchResult(data, networkError)
    }

//    fun requestMore(immutableQuery: String) {
//        requestAndSaveData(immutableQuery)
//    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}