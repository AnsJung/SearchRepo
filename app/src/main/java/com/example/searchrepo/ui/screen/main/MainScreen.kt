package com.example.searchrepo.ui.screen.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.searchrepo.ui.components.RepoItem
import com.example.searchrepo.ui.components.SearchTextField
import com.example.searchrepo.ui.screen.detail.DetailRepoModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToDetail: (DetailRepoModel) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    MainScreen(
        state,
        onSearchTextChanged = viewModel::onSearchTextChanged,
        onSearchClick = viewModel::requestRepoList,
        onNavigateToDetail = { id ->
            val detailRepoModel = viewModel.getDetailItem(id)
            if (detailRepoModel != null) {
                onNavigateToDetail(detailRepoModel)
            } else {
                Toast.makeText(context, "다음 화면으로 이동할 수 없습니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    )
}

@Composable
private fun MainScreen(
    state: RepoUiState,
    onSearchTextChanged: (String) -> Unit,
    onSearchClick: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
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
            RepoList(state.repos, onNavigateToDetail)
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            if (state.error != null && !state.isLoading) {
                GuideText(text = "에러 발생: ${state.error}")
            } else {
                if (!state.hasSearched) {
                    GuideText("검색어를 입력해주세요")
                }

                if (state.hasSearched && !state.isLoading && state.repos.isEmpty()) {
                    GuideText("검색 결과가 없습니다")
                }
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
fun RepoList(repos: List<MainRepoModel>, onNavigateToDetail: (Int) -> Unit) {
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
            RepoItem(repo, Modifier.clickable {
                Log.e("JH", "repo id >> ${repo.id}")
                onNavigateToDetail(repo.id)
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    MaterialTheme {
        MainScreen(
            state = RepoUiState(
                hasSearched = true,
                repos = listOf(
                    MainRepoModel(
                        1,
                        "Sample Project",
                        "Javas",
                        "Java",
                        5000,
                        1000,
                        "Junghyeon",
                        "",
                        "",
                    ),
                    MainRepoModel(
                        2,
                        "Compose Study",
                        "Search Repo",
                        "Kotlin",
                        100,
                        500,
                        "Android",
                        "",
                        "",
                    )
                )
            ),
            onSearchTextChanged = {},
            onSearchClick = {},
            onNavigateToDetail = {} // 이제 (Int) -> Unit 타입에 맞게 동작
        )
    }
}