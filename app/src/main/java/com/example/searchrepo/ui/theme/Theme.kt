package com.example.searchrepo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GitHubDarkAccentBlue,      // 레포지토리 이름
    onPrimary = Color.Black,

    background = GitHubDarkBg,           // 전체 배경
    onBackground = GitHubDarkTextPrimary, // "GitHub 레포지토리 검색" 제목

    surface = GitHubDarkSurface,         // 카드 배경
    onSurface = GitHubDarkTextPrimary,    // 카드 내 메인 텍스트

    surfaceVariant = GitHubDarkSurface,   // 리스트 아이템 배경
    onSurfaceVariant = GitHubDarkTextSecondary, // 카드 내 설명 및 하단 정보

    outline = GitHubDarkBorder,           // 카드 테두리 (Border)

    secondaryContainer = GitHubDarkTagBg, // 토픽 태그 배경
    onSecondaryContainer = GitHubDarkTagText, // 토픽 태그 텍스트
    tertiary = GitHubDarkTFBg
)

private val LightColorScheme = lightColorScheme(
    primary = GitHubBlue,                // 프로젝트 이름 (Blue)
    onPrimary = Color.White,

    background = Color.White,            // 전체 배경
    onBackground = GitHubDark,           // 메인 텍스트 ("GitHub 레포지토리 검색", 유저명 등)

    surfaceVariant = GitHubBgLight,      // 리스트 영역 배경 (연한 회색)
    onSurfaceVariant = GitHubSecondary,  // 보조 텍스트 (날짜, 스타 수, 가이드 문구)

    outlineVariant = GitHubBorder,        // 아이템 테두리 선

    secondaryContainer = GitHubTopicTagBg,
    onSecondaryContainer = GitHubTopicTagText,
    tertiary = Color.White
)
@Composable
fun SearchRepoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // GitHub 테마를 고정해서 사용하기 위해 기본값을 false로 변경하는 것을 권장합니다.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // 상태바 색상 제어 로직 추가
    val view = LocalView.current
// Theme.kt의 SearchRepoTheme 내부
    SideEffect {
        val window = (view.context as Activity).window
        val insetsController = WindowCompat.getInsetsController(window, view)

        // 배경색 설정은 이제 의미가 없으므로 아이콘 색상에만 집중합니다.
        insetsController.isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}