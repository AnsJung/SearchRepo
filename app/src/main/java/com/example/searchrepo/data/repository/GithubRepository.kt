package com.example.searchrepo.data.repository

import androidx.paging.PagingData
import com.example.searchrepo.ui.common.ApiResult
import com.example.searchrepo.ui.model.RepoUiModel
import com.example.searchrepo.ui.screen.main.MainRepoModel
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun getSearchRepoPaging(query: String): Flow<PagingData<RepoUiModel>>
}