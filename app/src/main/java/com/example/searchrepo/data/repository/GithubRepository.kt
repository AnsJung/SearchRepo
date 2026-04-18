package com.example.searchrepo.data.repository

import androidx.paging.PagingData
import com.example.searchrepo.ui.model.RepoOriginModel
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun getSearchRepoPaging(query: String): Flow<PagingData<RepoOriginModel>>
}