package com.example.searchrepo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.searchrepo.data.api.GithubAPI
import com.example.searchrepo.data.model.toUiModel
import com.example.searchrepo.ui.model.RepoUiModel

class GithubPagingSource(
    private val githubApi: GithubAPI,
    private val query: String
) : PagingSource<Int, RepoUiModel>() {

    // 데이터 로드
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoUiModel> {
        // 로드할 페이지 번호, 초기값은 null이므로 1로 설정
        val page = params.key ?: 1

        return try {
            // 서버에 몇번째 페이지를 몇 개 가져올지 전달
            val response = githubApi.searchRepos(query, page, params.loadSize)
            val items = response.items.map { it.toUiModel() }
            // 결과 반환
            LoadResult.Page(
                data = items,
                // 1페이지면 더 이상 없으므로 null
                prevKey = if (page == 1) null else page - 1,
                // 가져온 데이터가 없거나 설정된 개수보다 적으면 null
                nextKey = if (items.isEmpty() || items.size < params.loadSize) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    // 데이터가 무효화되었을 때 다시 시작할 키를 결정하는 함수
    override fun getRefreshKey(state: PagingState<Int, RepoUiModel>): Int? {
        // 스크롤 위치(anchorPosition)를 기준으로 가장 가까운 페이지의 키를 반환합니다.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}