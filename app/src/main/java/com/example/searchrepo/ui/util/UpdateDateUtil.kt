package com.example.searchrepo.ui.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun String.toRelativeTime(): String {
    return try {
        val targetDate = ZonedDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
        val now = ZonedDateTime.now(targetDate.zone)

        // 날짜 단위 차이 (만 나이 계산하듯 시/분/초 무시하고 '날짜'만 비교)
        val daysBetween = ChronoUnit.DAYS.between(targetDate.toLocalDate(), now.toLocalDate())

        // 개월/년 단위는 전체 시간 차이로 계산
        val years = ChronoUnit.YEARS.between(targetDate, now)
        val months = ChronoUnit.MONTHS.between(targetDate, now)

        when {
            years > 0 -> "${years}년 전"
            months > 0 -> "${months}개월 전"
            daysBetween > 1 -> "${daysBetween}일 전"
            daysBetween == 1L -> "어제"
            daysBetween == 0L -> "오늘" // 1시간 전, 5분 전 모두 "오늘"로 표시
            else -> "오늘" // 미래 날짜 대응
        }
    } catch (e: Exception) {
        this
    }
}