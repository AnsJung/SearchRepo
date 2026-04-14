package com.example.searchrepo.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.searchrepo.data.paging.GithubPagingSource
import com.example.searchrepo.data.api.GithubAPI
import com.example.searchrepo.data.model.toUiModel
import com.example.searchrepo.ui.common.ApiResult
import com.example.searchrepo.ui.model.RepoUiModel
import com.example.searchrepo.ui.screen.main.MainRepoModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val githubAPI: GithubAPI
) : GithubRepository {

    // 스크롤에 따라 계속 요청하는 스트림 방식이기때문에 Flow를 사용
    override fun getSearchRepoPaging(query: String): Flow<PagingData<RepoUiModel>> {
        // Pager 페이징의 공정
        return Pager(
            config = PagingConfig(
                pageSize = 20,              // 한 페이지당 로드할 개수
                enablePlaceholders = false, // 실제 데이터가 오기 전 빈 칸 표시 여부
                initialLoadSize = 20        // 첫 페이지 로드 개수
            ),
            pagingSourceFactory = { GithubPagingSource(githubAPI, query) }
        ).flow
    }
}