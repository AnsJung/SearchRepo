package com.example.searchrepo.data.model

import com.google.gson.annotations.SerializedName

data class ResponseRepos(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    val items: List<Repo>
)

data class Repo(
    val id: Int,
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    val description: String?,
//    @SerializedName("html_url")
//    val htmlUrl: String
    val language: String?,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    @SerializedName("forks_count")
    val forksCount: Int,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class Owner(
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)