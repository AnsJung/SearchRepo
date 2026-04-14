package com.example.searchrepo.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchrepo.ui.screen.main.RepoUiState

@Composable
fun SearchTextField(
    state: RepoUiState,
    onSearchTextChanged: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            // 1. 배경색을 테마에 맞게 (라이트: 흰색, 다크: 짙은 네이비)
            .padding(top = 15.dp, start = 10.dp, end = 10.dp)
            // 2. 테두리 색상도 테마의 outlineVariant를 따름
            .background(
                color = MaterialTheme.colorScheme.tertiary, // Color(0xFF364152)
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            // 3. 아이콘 색상도 테마에 반응하도록 설정
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextField(
            value = state.searchText,
            placeholder = {
                Text(
                    text = "레포지토리 검색...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            },
            onValueChange = onSearchTextChanged,
            textStyle = TextStyle(
                // 4. 입력되는 글자 색상
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    Log.e("JH", "검색 버튼 클릭")
                    focusManager.clearFocus()
                    onSearchClick()
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                // 5. 커서 색상은 포인트 컬러인 Blue로 유지
                cursorColor = MaterialTheme.colorScheme.primary,
                // 입력 텍스트 색상도 명시적으로 지정 가능
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}