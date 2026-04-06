package com.example.searchrepo.data

import com.example.searchrepo.data.model.ResponseRepos
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubAPI {

    @GET("search/repositories")
    suspend fun searchRepos(
        @Query("q") query: String
    ): ResponseRepos
}