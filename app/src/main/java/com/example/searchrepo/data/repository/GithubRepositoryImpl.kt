package com.example.searchrepo.data.repository

import android.util.Log
import com.example.searchrepo.data.api.GithubAPI
import com.example.searchrepo.data.model.toUiModel
import com.example.searchrepo.ui.common.ApiResult
import com.example.searchrepo.ui.model.RepoUiModel
import com.example.searchrepo.ui.screen.main.MainRepoModel
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val githubAPI: GithubAPI
) : GithubRepository {
    override suspend fun requestRepoList(query: String): ApiResult<List<RepoUiModel>> {
        return try {
            Log.e("JH","요청 시작 - $query")
            val response = githubAPI.searchRepos(query)
            Log.e("JH","response >> $response")
            val uiModels = response.items.map { it.toUiModel() }
            Log.e("JH","uiModels >> $uiModels")
            ApiResult.Success(uiModels)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "에러 발생")
        }
    }
}