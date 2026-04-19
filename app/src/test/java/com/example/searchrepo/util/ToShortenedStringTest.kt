package com.example.searchrepo.util

import com.example.searchrepo.ui.util.toShortenedString
import junit.framework.TestCase.assertEquals
import org.junit.Test


class ToShortenedStringTest {

    @Test
    fun `1000이상이면 K단위로 변환된다`() {
        val result = 1500.toShortenedString()
        assertEquals("1.5k", result)
    }

    @Test
    fun `1000 미만이면 그대로 반환된다`() {
        val result = 999.toShortenedString()
        assertEquals("999", result)
    }

    @Test
    fun `정확히 1000이면 1_0K로 변환된다`() {
        val result = 1000.toShortenedString()
        assertEquals("1.0k", result)
    }
}