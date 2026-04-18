package com.example.searchrepo.ui.screen.detail

import android.content.Intent
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.searchrepo.R
import com.example.searchrepo.ui.util.GithubUtil
import com.example.searchrepo.ui.util.toKoreanDate
import com.example.searchrepo.ui.util.toShortenedString

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
) {
    val isFavorite by detailViewModel.isFavorite.collectAsState()
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(MaterialTheme.colorScheme.background),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack, contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                onBackClick()
                            }
                            .padding(start = 15.dp)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        detailViewModel.detailRepoModel.projectName,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        painter =
                            if (isFavorite) {
                                painterResource(R.drawable.ic_git_heart_filled)
                            } else {
                                painterResource(R.drawable.ic_git_heart_border)
                            },
                        contentDescription = "",
                        Modifier
                            .size(20.dp)
                            .clickable {
                                detailViewModel.toggleFavoriteItem()
                            },
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(20.dp))
                }
            })
        { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(top=paddingValues.calculateTopPadding())
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 15.dp)
                    .padding(top = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OwnerArea(
                    detailViewModel.detailRepoModel.avatarUrl,
                    detailViewModel.detailRepoModel.userName
                )
                if (!detailViewModel.detailRepoModel.description.isNullOrBlank()) {
                    DescriptionArea(detailViewModel.detailRepoModel.description)
                }
                if (detailViewModel.detailRepoModel.topics.isNotEmpty()) {
                    TopicsArea(detailViewModel.detailRepoModel.topics)
                }
                CountArea(
                    detailViewModel.detailRepoModel.stargazersCount,
                    detailViewModel.detailRepoModel.forksCount,
                    detailViewModel.detailRepoModel.watchersCount,
                    detailViewModel.detailRepoModel.openIssuesCount
                )
                InfoArea(
                    detailViewModel.detailRepoModel.language,
                    detailViewModel.detailRepoModel.defaultBranch,
                    detailViewModel.detailRepoModel.license,
                    detailViewModel.detailRepoModel.createdAt,
                    detailViewModel.detailRepoModel.updatedAt
                )
                MoveArea(
                    detailViewModel.detailRepoModel.htmlUrl
                )
            }
        }
    }

}

@Composable
fun OwnerArea(avatarUrl: String, userName: String) {
    var isImageVisible by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(16.dp)
    )
    {
        if (isImageVisible) {
            AsyncImage(
                model = avatarUrl,
                error = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "유저 아이콘",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                onError = { error ->
                    Log.e("JH", error.result.throwable.toString())
                    isImageVisible = false
                }
            )

        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                "소유자",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                userName,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun DescriptionArea(description: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            description,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp
        )
    }
}

@Composable
fun TopicsArea(topics: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            "토픽", color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(10.dp))
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            topics.forEach { text ->
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = CircleShape
                        )
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun CountArea(
    stargazersCount: Int,
    forksCount: Int,
    watchersCount: Int,
    openIssuesCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            StatItem(
                iconResId = R.drawable.ic_git_star,
                label = "Stars",
                value = stargazersCount,
                modifier = Modifier.weight(1f)
            )
            StatItem(
                iconResId = R.drawable.ic_git_fork,
                iconColor = Color.Gray,
                label = "Forks",
                value = forksCount,
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            StatItem(
                iconResId = R.drawable.ic_git_eye,
                iconColor = Color.Gray,
                label = "Watchers",
                value = watchersCount,
                modifier = Modifier.weight(1f)
            )
            StatItem(
                iconResId = R.drawable.ic_issues,
                iconColor = Color.Gray,
                label = "Issues",
                value = openIssuesCount,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatItem(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int,
    iconColor: Color? = null,
    label: String,
    value: Int,
) {
    Row(
        modifier = modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = iconColor ?: Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            Text(text = value.toShortenedString(), color=MaterialTheme.colorScheme.onBackground,fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun InfoArea(
    language: String?,
    defaultBranch: String,
    license: String?,
    createdAt: String,
    updatedAt: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(16.dp),
    ) {
        language?.let {
            InfoRow(
                languageColor = GithubUtil.getLanguageColor(it), // C 언어의 파란색 점
                label = "언어",
                value = it
            )
        }
        InfoRow(
            iconResId = R.drawable.ic_git_branch, // 아까 저장한 브랜치 아이콘
            label = "기본 브랜치",
            value = defaultBranch
        )
        license?.let {
            InfoRow(
                // 라이선스 아이콘이 별도로 없다면 ic_issues 등을 활용하거나 빈 박스 처리
                iconResId = R.drawable.ic_issues,
                label = "라이선스",
                value = it
            )
        }
        InfoRow(
            iconResId = R.drawable.ic_calendar, // 아까 저장한 달력 아이콘
            label = "생성일",
            value = createdAt.toKoreanDate()
        )
        InfoRow(
            iconResId = R.drawable.ic_calendar,
            label = "최근 업데이트",
            value = updatedAt.toKoreanDate()
        )
    }
}

@Composable
fun InfoRow(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int? = null,
    languageColor: Color? = null, // 첫 번째 항목의 파란 점을 위한 처리
    label: String,
    value: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 아이콘 혹은 언어 색상 점 표시
        Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
            if (languageColor != null) {
                // '언어' 항목의 경우 파란색 원형 점
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(color = languageColor, shape = CircleShape)
                )
            } else if (iconResId != null) {
                // 나머지 항목의 경우 SVG 아이콘
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant, // 아이콘은 타이틀과 같은 회색톤
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 타이틀 (회색톤)
        Text(
            text = "$label:",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            modifier = Modifier.padding(end = 8.dp)
        )

        // 내용 (검정/배경 대비색)
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MoveArea(htmlUrl: String) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    htmlUrl.toUri()
                )
                context.startActivity(intent)
            }
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Github에서 보기", color = MaterialTheme.colorScheme.onBackground, fontSize = 16.sp)
        Icon(
            painter = painterResource(R.drawable.ic_external_link), tint = Color.Gray,
            contentDescription = "페이지로 이동",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true, name = "GitHub 상세 화면 프리뷰")
@Composable
private fun DetailScreenPreview() {
    // JSON의 첫 번째 아이템 데이터를 기반으로 생성
    val previewData = DetailRepoModel(
        projectName = "AnimationCalendar",
        userName = "AnsJung",
        avatarUrl = "https://avatars.githubusercontent.com/u/77667819?v=4",
        description = null,
        stargazersCount = 1000,
        forksCount = 20000,
        watchersCount = 100,
        openIssuesCount = 30,
        language = "Kotlin",
        defaultBranch = "master",
        license = null,
        createdAt = "2026-02-26T08:05:12Z",
        updatedAt = "2026-02-26T08:25:15Z",
        topics = emptyList(),
        htmlUrl = "https://github.com/AnsJung/AnimationCalendar"
    )

    MaterialTheme {
        DetailScreen(
            onBackClick = { /* 테스트 시 로그 출력 등 */ }
        )
    }
}