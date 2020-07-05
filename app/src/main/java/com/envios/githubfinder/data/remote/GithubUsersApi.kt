package com.envios.githubfinder.data.remote

import com.envios.githubfinder.BuildConfig
import com.envios.githubfinder.data.remote.model.GithubUser
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubUsersApi {

    @GET("search/users")
    @Headers("Content-Type: application/json", "Authorization: token " + BuildConfig.TOKEN_API)
    fun searchAsync(@Query("q") q: String, @Query("page") page: Int, @Query("per_page") perPage: Int): Deferred<Response<GithubUser>>

}