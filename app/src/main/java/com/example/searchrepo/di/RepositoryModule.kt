package com.example.searchrepo.di

import com.example.searchrepo.data.local.PreferenceManager
import com.example.searchrepo.data.local.PreferenceRepository
import com.example.searchrepo.data.repository.GithubRepository
import com.example.searchrepo.data.repository.GithubRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGithubRepository(
        githubRepositoryImpl: GithubRepositoryImpl
    ): GithubRepository

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        preferenceManager: PreferenceManager
    ): PreferenceRepository
}