package dev.vkgo.pagination.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.vkgo.pagination.model.Repo

@Database(
    entities = [Repo::class],
    version = 1,
    exportSchema = false
)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao

    companion object {
        @Volatile
        private var instance: RepoDatabase? = null

        fun getInstance(context: Context): RepoDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                    context.applicationContext,
                    RepoDatabase::class.java, "Github.db"
                )
                .build()

    }
}