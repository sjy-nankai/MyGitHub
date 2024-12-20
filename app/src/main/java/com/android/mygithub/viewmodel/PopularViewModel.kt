package com.android.mygithub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.github.data.remote.di.ApiModule
import com.android.github.domain.model.Repository
import com.android.github.domain.usecase.GithubUseCase
import com.android.mygithub.data.RepositoriesUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PopularViewModel(
    private val useCase: GithubUseCase = GithubUseCase(repository = ApiModule.repository),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private var _uiState =
        MutableStateFlow<RepositoriesUiState<List<Repository>>>(RepositoriesUiState.Loading)
    val uiState
        get() = _uiState
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("my-github", exception.toString())
    }

    fun loadPopularRepos(language: String? = null, since: String = "weekly") {
        _uiState.value = RepositoriesUiState.Loading
        viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
            useCase.getPopularRepos(language, since)
                .onSuccess { repos ->
                    _uiState.value = RepositoriesUiState.Success(repos)
                }
                .onFailure { error ->
                    _uiState.value = RepositoriesUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}