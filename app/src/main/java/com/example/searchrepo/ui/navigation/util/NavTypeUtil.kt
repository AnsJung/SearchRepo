package com.example.searchrepo.ui.navigation.util

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json

inline fun <reified T : Any> createNaveType(
    isNullableAllowed: Boolean = false
): NavType<T> = object : NavType<T>(isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)
            ?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): T {
        // Uri.decode를 통해 URL 인코딩된 문자열을 원래 JSON으로 복구
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun serializeAsValue(value: T): String {
        // JSON 문자열을 URL에 안전하게 포함시키기 위해 인코딩
        return Uri.encode(Json.encodeToString(value))
    }
}