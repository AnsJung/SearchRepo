package com.example.searchrepo.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchrepo.data.GithubAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    githubAPI: GithubAPI
) : ViewModel() {
    init {
        viewModelScope.launch {
            try {
                val repo = githubAPI.searchRepos("AnimationCalendar")
                Log.e("JH",repo.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}