package dev.vkgo.pagination.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import dev.vkgo.pagination.data.GithubRepository
import dev.vkgo.pagination.model.Repo
import dev.vkgo.pagination.model.RepoSearchResult

private const val VISIBLE_THRESHOLD = 5

class SearchViewModel(private val repository: GithubRepository) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()
    private val repoResult: LiveData<RepoSearchResult> = Transformations.map(queryLiveData) {
        repository.search(it)
    }

    val repos: LiveData<PagedList<Repo>> = Transformations.switchMap(repoResult) { it.data }
    val networkError: LiveData<String> = Transformations.switchMap(repoResult) { it.networkErrors }

    fun searchRepo(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun laseQuery() = queryLiveData.value

//    fun onListScroll(lastItem: Int, visibleItemCount: Int, totalItemCount: Int) {
//        if (lastItem + visibleItemCount + VISIBLE_THRESHOLD >= totalItemCount) {
//            val immutableQuery = laseQuery()
//            if (immutableQuery != null) {
//                repository.requestMore(immutableQuery)
//            }
//        }
//    }

}