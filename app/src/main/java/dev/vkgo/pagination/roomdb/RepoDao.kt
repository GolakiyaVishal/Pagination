package dev.vkgo.pagination.roomdb

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.vkgo.pagination.model.Repo

@Dao
interface RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Repo>)

    @Query("SELECT * FROM repos WHERE (name LIKE :queryString) OR (description LIKE :queryString) ORDER BY stars DESC, name ASC")
    fun reposByName(queryString: String): DataSource.Factory<Int, Repo>
//    fun reposByName(queryString: String): LiveData<List<Repo>>
}