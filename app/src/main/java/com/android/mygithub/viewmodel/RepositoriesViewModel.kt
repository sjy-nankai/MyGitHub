package com.android.mygithub.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.github.data.remote.di.ApiModule
import com.android.github.domain.model.Repository
import com.android.github.domain.usecase.GithubUseCase
import com.android.github.utils.AuthPreferences
import com.android.mygithub.data.RepositoriesUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepositoriesViewModel(
    private val useCase: GithubUseCase = GithubUseCase(repository = ApiModule.repository),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<RepositoriesUiState<List<Repository>>>(RepositoriesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadRepositories(context: Context) {
        viewModelScope.launch(dispatcher) {
            val token = AuthPreferences.getAccessToken(context) ?: ""
            useCase.getAuthenticatedUserRepos(token).onSuccess { repos ->
                _uiState.value = RepositoriesUiState.Success(repos)
            }.onFailure { e ->
                _uiState.value = RepositoriesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
