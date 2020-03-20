package dev.vkgo.pagination.api

import com.google.gson.annotations.SerializedName
import dev.vkgo.pagination.model.Repo

data class RepoSearchResponse(
    @field:SerializedName("total_count") val total: Int = 0,
    @field:SerializedName("items") val items: List<Repo> = emptyList(),
    val nextPage: Int? = null
)