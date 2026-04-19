package com.example.searchrepo

import androidx.paging.PagingSource
import com.example.searchrepo.data.api.FakeGithubAPI
import com.example.searchrepo.data.model.Owner
import com.example.searchrepo.data.model.Repo
import com.example.searchrepo.data.model.ResponseRepos
import com.example.searchrepo.data.paging.GithubPagingSource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PagingTest {
    @Test
    fun `정상 데이터면 LoadResult_Page 반환`() = runTest {
        val fakeApi = FakeGithubAPI()

        fakeApi.response = ResponseRepos(
            totalCount = 1,
            incompleteResults = false,
            items = listOf(
                Repo(
                    id = 1,
                    name = "TestRepo",
                    description = "desc",
                    htmlUrl = "url",
                    language = "Kotlin",
                    stargazersCount = 10,
                    forksCount = 5,
                    owner = Owner(
                        login = "jun",
                        avatarUrl = ""
                    ),
                    updatedAt = "2024-01-01T00:00:00Z",
                    topics = emptyList(),
                    watchersCount = 3,
                    openIssuesCount = 1,
                    license = null,
                    defaultBranch = "main",
                    createdAt = "2024-01-01T00:00:00Z"
                )
            )
        )

        val pagingSource = GithubPagingSource(fakeApi, "android")

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // 타입 검증
        assert(result is PagingSource.LoadResult.Page)

        val page = result as PagingSource.LoadResult.Page

        // 데이터 검증
        assertEquals(1, page.data.size)
        assertEquals(1, page.data.first().id)
    }

    @Test
    fun `빈 데이터면 빈 LoadResult_Page 반환`() = runTest {
        val fakeApi = FakeGithubAPI()
        fakeApi.response = ResponseRepos(
            totalCount = 0,
            incompleteResults = false,
            items = emptyList()
        )

        val pagingSource = GithubPagingSource(fakeApi, "android")

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Page)

        val page = result as PagingSource.LoadResult.Page
        assertTrue(page.data.isEmpty())
    }

    @Test
    fun `에러 발생하면 LoadResult_Error 반환`() = runTest {
        val fakeApi = FakeGithubAPI()
        fakeApi.shouldThrowError = true

        val pagingSource = GithubPagingSource(fakeApi, "android")

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Error)
    }
}