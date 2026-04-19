package com.example.searchrepo.data.api

import com.example.searchrepo.data.model.ResponseRepos

class FakeGithubAPI : GithubAPI {

    var shouldThrowError = false
    var response: ResponseRepos = ResponseRepos(
        totalCount = 0,
        incompleteResults = false,
        items = emptyList()
    )

    override suspend fun searchRepos(
        query: String,
        page: Int,
        perPage: Int
    ): ResponseRepos {
        if (shouldThrowError) {
            throw RuntimeException("테스트 에러")
        }
        return response
    }
}