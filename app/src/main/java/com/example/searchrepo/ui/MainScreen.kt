package com.example.searchrepo.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MainScreen(viewModel: RepoViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = 10.dp)
    ) {
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        Text(
            "GitHub 레포지토리 검색",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp)
        )
        SearchTextField(state, viewModel)
        Spacer(Modifier.height(15.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFF1F3F5))
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            // 결과 리스트
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
fun SearchTextField(state: RepoUiState, viewModel: RepoViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 15.dp, start = 10.dp, end = 10.dp)
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search"
        )
        TextField(
            value = state.searchText,
            onValueChange = {
                viewModel.onSearchTextChanged(it)
            },
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 15.sp
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // 검색 로직 추가
                    Log.e("JH", "검색 버튼 클릭")
                    viewModel.requestRepoList()
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }
}

@Composable
fun BoxScope.GuideText(text: String) {
    Text(
        text,
        color = Color(0xFF6C757D),
        fontSize = 18.sp,
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(50.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}