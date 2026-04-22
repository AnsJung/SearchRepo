package com.example.searchrepo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.searchrepo.data.api.GithubAPI
import com.example.searchrepo.ui.model.RepoOriginModel
import com.example.searchrepo.ui.model.toUiModel
import kotlin.coroutines.cancellation.CancellationException

private const val PAGE_SIZE = 20
private const val GITHUB_SEARCH_MAX_RESULTS = 1000

class GithubPagingSource(
    private val githubApi: GithubAPI,
    private val query: String
) : PagingSource<Int, RepoOriginModel>() {

    // 데이터 로드
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoOriginModel> {
        // 로드할 페이지 번호, 초기값은 null이므로 1로 설정
        val page = params.key ?: 1

        return try {
            // 서버에 몇번째 페이지를 몇 개 가져올지 전달
            val response = githubApi.searchRepos(query, page, PAGE_SIZE)
            val items = response.items.map { it.toUiModel() }
            val loadedCount = page * PAGE_SIZE
            val reachedApiLimit = loadedCount >= GITHUB_SEARCH_MAX_RESULTS
            val reachedTotalCount = loadedCount >= response.totalCount
            // 결과 반환
            LoadResult.Page(
                data = items,
                // 1페이지면 더 이상 없으므로 null
                prevKey = if (page == 1) null else page - 1,
                // 가져온 데이터가 없거나 설정된 개수보다 적으면 null
                nextKey = if (
                    items.isEmpty() ||
                    items.size < PAGE_SIZE ||
                    reachedApiLimit ||
                    reachedTotalCount
                ) {
                    null
                } else {
                    page + 1
                }
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    // 데이터가 무효화되었을 때 다시 시작할 키를 결정하는 함수
    override fun getRefreshKey(state: PagingState<Int, RepoOriginModel>): Int? {
        // 스크롤 위치(anchorPosition)를 기준으로 가장 가까운 페이지의 키를 반환합니다.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
