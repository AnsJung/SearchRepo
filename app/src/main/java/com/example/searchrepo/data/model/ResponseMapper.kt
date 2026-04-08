package com.example.searchrepo.data.model

import com.example.searchrepo.ui.model.RepoUiModel

fun Repo.toUiModel(): RepoUiModel {
    return RepoUiModel(
        id = this.id,
        projectName = this.name,
        fullName = this.fullName,
        description = this.description ?: "",
        language = this.language ?: "",
        stargazersCount = this.stargazersCount,
        forksCount = this.forksCount,
        userName = this.owner.login,
        avatarUrl = this.owner.avatarUrl,
//        updatedAt = formatGitHubDate(this.updatedAt)
        updatedAt = ""
    )
}