package com.example.searchrepo.ui.util

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

object GithubUtil {
    val gitHubLanguageColors = mapOf(
        "Kotlin" to Color(0xFFA97BFF),
        "Java" to Color(0xFFB07219),
        "Python" to Color(0xFF3572A5),
        "JavaScript" to Color(0xFFF1E05A),
        "TypeScript" to Color(0xFF3178C6),
        "C++" to Color(0xFFF34B7D),
        "C" to Color(0xFF555555),
        "C#" to Color(0xFF178600),
        "Ruby" to Color(0xFF701516),
        "Go" to Color(0xFF00ADD8),
        "Swift" to Color(0xFFF05138),
        "Dart" to Color(0xFF00B4AB),
        "PHP" to Color(0xFF4F5D95),
        "HTML" to Color(0xFFE34C26),
        "CSS" to Color(0xFF563D7C),
        "Rust" to Color(0xFFDEA584),
        "Objective-C" to Color(0xFF438EFF),
        "Shell" to Color(0xFF89E051),
        "Vue" to Color(0xFF41B883),
        "Jupyter Notebook" to Color(0xFFDA5B0B)
    )

    /**
     * 언어 이름을 넣으면 색상을 반환해주는 확장/유틸 함수
     * @param language GitHub API에서 받은 언어 이름 (예: "Kotlin")
     * @return 매칭되는 색상이 없으면 기본값(회색)을 반환합니다.
     */
    fun getLanguageColor(language: String?): Color {
        // 값이 null이거나 Map에 없는 희귀 언어일 경우 보여줄 기본 색상 (Fallback)
        return gitHubLanguageColors[language] ?: Color.LightGray
    }
}