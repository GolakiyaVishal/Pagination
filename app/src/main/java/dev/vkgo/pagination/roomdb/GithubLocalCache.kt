package dev.vkgo.pagination.roomdb

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import dev.vkgo.pagination.model.Repo
import java.util.concurrent.Executor

class GithubLocalCache(
    private val repoDao: RepoDao,
    private val ioExecutor: Executor
) {
    fun insert(repos: List<Repo>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("GithubLocalCache", "inserting ${repos.size} repos")
            repoDao.insert(repos)
            insertFinished()
        }
    }

    fun reposByName(name: String): DataSource.Factory<Int, Repo> {
        val query = "%${name.replace(' ', '%')}%"
        return repoDao.reposByName(query)
    }
}