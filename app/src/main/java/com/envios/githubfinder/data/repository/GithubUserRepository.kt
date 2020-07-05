package com.envios.githubfinder.data.repository

import com.envios.githubfinder.data.remote.GithubUsersApi
import com.envios.githubfinder.data.remote.model.GithubUser
import retrofit2.Response


class GithubUserRepository (private val githubUsersApi: GithubUsersApi) {


    suspend fun searchUsers(username: String, page: Int, perPage: Int): Response<GithubUser> {
        return githubUsersApi.searchAsync(username, page, perPage).await()
    }


}