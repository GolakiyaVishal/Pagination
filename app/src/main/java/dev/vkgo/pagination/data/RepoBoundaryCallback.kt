package dev.vkgo.pagination.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import dev.vkgo.pagination.api.GithubService
import dev.vkgo.pagination.api.searchRepos
import dev.vkgo.pagination.model.Repo
import dev.vkgo.pagination.roomdb.GithubLocalCache

class RepoBoundaryCallback(
    private val query: String,
    private val service: GithubService,
    private val cache: GithubLocalCache
) : PagedList.BoundaryCallback<Repo>() {

    private var lastRequestedPage = 1
    private var isRequestInProgress = false
    private val _networkError = MutableLiveData<String>()

    val networkError: LiveData<String> get() = _networkError

    override fun onZeroItemsLoaded() {
//        super.onZeroItemsLoaded()
        requestAndSaveData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Repo) {
//        super.onItemAtEndLoaded(itemAtEnd)
        requestAndSaveData(query)
    }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        isRequestInProgress = true
        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            cache.insert(repos) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            _networkError.postValue(error)
            isRequestInProgress = false
        })
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}