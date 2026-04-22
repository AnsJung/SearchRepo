package com.example.searchrepo.data.local

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteRepoPreference(
    val id: Int,
    val projectName: String,
    val htmlUrl: String,
    val userName: String,
    val avatarUrl: String,
    val description: String?,
    val language: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val updatedAt: String,
    val topics: List<String> = emptyList(),
    val watchersCount: Int = 0,
    val openIssuesCount: Int = 0,
    val licenseName: String? = null,
    val defaultBranch: String = "",
    val createdAt: String = ""
)
