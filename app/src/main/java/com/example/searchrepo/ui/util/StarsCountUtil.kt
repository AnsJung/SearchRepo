package com.example.searchrepo.ui.util

import kotlin.math.roundToInt

fun Int.toShortenedString(): String {
    return if (this >= 1000) {
        // 1. 숫자를 1000으로 나눈 뒤 소수점 첫째 자리까지 남깁니다.
        // 2. 10을 곱하고 반올림 후 다시 10.0으로 나누어 소수점 한 자리를 고정합니다.
        val shortened = (this / 100.0).roundToInt() / 10.0
        "${shortened}k"
    } else {
        // 1000 미만은 그대로 반환
        this.toString()
    }
}