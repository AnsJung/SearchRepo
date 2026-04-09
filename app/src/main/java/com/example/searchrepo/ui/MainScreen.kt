package com.example.searchrepo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.searchrepo.ui.components.RepoItem
import com.example.searchrepo.ui.components.SearchTextField
import com.example.searchrepo.ui.model.RepoUiModel

@Composable
fun MainScreen(viewModel: RepoViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    MainScreen(
        state,
        onSearchTextChanged = viewModel::onSearchTextChanged,
        onSearchClick = viewModel::requestRepoList
    )
}

@Composable
private fun MainScreen(
    state: RepoUiState,
    onSearchTextChanged: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 10.dp)
    ) {
        Text(
            "GitHub 레포지토리 검색",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp)
        )
        SearchTextField(
            state,
            onSearchTextChanged = onSearchTextChanged,
            onSearchClick = onSearchClick
        )
        Spacer(Modifier.height(15.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            RepoList(state.repos)
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (!state.hasSearched) {
                GuideText("검색어를 입력해주세요")
            }

            if (state.hasSearched && !state.isLoading && state.repos.isEmpty()) {
                GuideText("검색 결과가 없습니다")
            }
        }
    }
}


@Composable
fun BoxScope.GuideText(text: String) {
    Text(
        text,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 18.sp,
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(50.dp)
    )
}

@Composable
fun RepoList(repos: List<RepoUiModel>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = 50.dp,
            top = 5.dp
        )
    ) {
        items(items = repos, key = { repo ->
            repo.id
        }) { repo ->
            RepoItem(repo)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    MaterialTheme {
        MainScreen(
            state = RepoUiState(),
            onSearchTextChanged = {},
            onSearchClick = {}
        )
    }
}

