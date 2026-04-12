package com.example.searchrepo.data.repository

import com.example.searchrepo.ui.common.ApiResult
import com.example.searchrepo.ui.model.RepoUiModel
import com.example.searchrepo.ui.screen.main.MainRepoModel

interface GithubRepository {
    suspend fun requestRepoList(query: String): ApiResult<List<RepoUiModel>>
}