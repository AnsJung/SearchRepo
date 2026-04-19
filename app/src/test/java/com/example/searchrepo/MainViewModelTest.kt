package com.example.searchrepo

import com.example.searchrepo.data.local.FakePreferenceRepository
import com.example.searchrepo.data.repository.FakeGithubRepository
import com.example.searchrepo.ui.screen.main.MainViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MainViewModelTest {

    @Test
    fun `검색어 입력하면 state에 반영된다`() {
        val viewModel = MainViewModel(
            githubRepository = FakeGithubRepository(),
            preferenceRepository = FakePreferenceRepository()
        )
        viewModel.onSearchTextChange("android")
        assertEquals("android", viewModel.uiState.value.searchText)
    }

    @Test
    fun `검색어 없을 때 refresh하면 다이얼로그가 열린다`() {
        val viewModel = MainViewModel(
            githubRepository = FakeGithubRepository(),
            preferenceRepository = FakePreferenceRepository()
        )

        viewModel.onSearchTextChange("")
        viewModel.refreshSearched()

        assertEquals(true, viewModel.uiState.value.showGuideDialog)
    }
}