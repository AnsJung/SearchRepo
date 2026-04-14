package com.example.searchrepo.data.api

import com.example.searchrepo.data.model.ResponseRepos
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubAPI {

    @GET("search/repositories")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int, // 페이지 번호
        @Query("per_page") perPage: Int // 한 페이지당 개수
    ): ResponseRepos
}