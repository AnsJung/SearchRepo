package com.example.searchrepo.ui.model

import com.example.searchrepo.data.model.Owner
import com.google.gson.annotations.SerializedName

data class RepoUiModel(
    val id: Int,
    val projectName: String,
    val fullName: String,
    val description: String?,
    val language: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val userName: String,
    val avatarUrl: String,
    val updatedAt: String
)
