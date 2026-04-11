package com.example.searchrepo.ui

import android.R.attr.label
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.searchrepo.R

@Composable
fun DetailScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
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
                        .padding(start = 15.dp)
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    "프로젝트명", fontWeight = FontWeight.Medium, fontSize = 20.sp,
                    color = Color(0xff0a0a0a)
                )
            }
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(paddingValues)
                .padding(horizontal = 15.dp)
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OwnerArea()
            DescriptionArea()
            TopicsArea()
            CountArea()
            InfoArea()
            MoveArea()
        }
    }
}

@Composable
fun OwnerArea() {
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("")
                .build(),
            error = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "유저 아이콘",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            onError = { error ->
                Log.e("JH", error.result.throwable.toString())
            }
        )
        Spacer(Modifier.width(5.dp))
        Column {
            Text(
                "소유자",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                "유저 네임", fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun DescriptionArea() {
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
            "Description", color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp
        )
    }
}

@Composable
fun TopicsArea() {
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
                .wrapContentHeight()
        ) {
            Text(
                text = "android",
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

@Composable
fun CountArea() {
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
                iconColor = Color(0xffEAB308),
                label = "Stars",
                value = "100",
                modifier = Modifier.weight(1f)
            )
            StatItem(
                iconResId = R.drawable.ic_git_fork,
                iconColor = Color.Gray,
                label = "Forks",
                value = "129",
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            StatItem(
                iconResId = R.drawable.ic_git_eye,
                iconColor = Color.Gray,
                label = "Watchers",
                value = "129",
                modifier = Modifier.weight(1f)
            )
            StatItem(
                iconResId = R.drawable.ic_issues,
                iconColor = Color.Gray,
                label = "Issues",
                value = "129",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatItem(
    @DrawableRes iconResId: Int,
    iconColor: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun InfoArea() {
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
        InfoRow(
            languageColor = Color(0xFF3572A5), // C 언어의 파란색 점
            label = "언어",
            value = "C"
        )
        InfoRow(
            iconResId = R.drawable.ic_git_branch, // 아까 저장한 브랜치 아이콘
            label = "기본 브랜치",
            value = "master"
        )
        InfoRow(
            // 라이선스 아이콘이 별도로 없다면 ic_issues 등을 활용하거나 빈 박스 처리
            iconResId = R.drawable.ic_issues,
            label = "라이선스",
            value = "Apache License 2.0"
        )
        InfoRow(
            iconResId = R.drawable.ic_calendar, // 아까 저장한 달력 아이콘
            label = "생성일",
            value = "2017년 11월 22일"
        )
        InfoRow(
            iconResId = R.drawable.ic_calendar,
            label = "최근 업데이트",
            value = "2026년 4월 11일"
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
fun MoveArea() {
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

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    MaterialTheme {
        DetailScreen()
    }
}