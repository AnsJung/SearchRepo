package com.example.searchrepo.ui.model

import com.example.searchrepo.data.model.Repo

fun Repo.toUiModel(): RepoOriginModel {
    return RepoOriginModel(
        id = this.id,
        projectName = this.name,
        description = this.description,
        language = this.language,
        stargazersCount = this.stargazersCount,
        forksCount = this.forksCount,
        userName = this.owner.login,
        avatarUrl = this.owner.avatarUrl,
        updatedAt = this.updatedAt,
        topics = this.topics,
        watchersCount = this.watchersCount,
        openIssuesCount = this.openIssuesCount,
        licenseName = this.license?.name,
        defaultBranch = this.defaultBranch,
        createdAt = this.createdAt,
        htmlUrl = this.htmlUrl
    )
}