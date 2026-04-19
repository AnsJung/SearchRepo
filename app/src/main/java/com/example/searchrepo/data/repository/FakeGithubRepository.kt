package com.example.searchrepo.data.repository

import androidx.paging.PagingData
import com.example.searchrepo.ui.model.RepoOriginModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGithubRepository : GithubRepository {

    var pagingData: Flow<PagingData<RepoOriginModel>> =
        flowOf(PagingData.empty())

    override fun getSearchRepoPaging(query: String): Flow<PagingData<RepoOriginModel>> {
        return pagingData
    }
}