package com.envios.githubfinder.data.remote.model


import com.google.gson.annotations.SerializedName

data class GithubUser(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean?,
    @SerializedName("items")
    val items: List<User?>?,
    @SerializedName("total_count")
    val totalCount: Int?
)