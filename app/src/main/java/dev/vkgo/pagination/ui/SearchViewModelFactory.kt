package dev.vkgo.pagination.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.vkgo.pagination.data.GithubRepository
import java.lang.RuntimeException

class SearchViewModelFactory(private val repository: GithubRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw (RuntimeException("unknown ViewModel class"))
    }
}