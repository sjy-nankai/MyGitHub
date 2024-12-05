package com.android.mygithub.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.github.data.remote.di.ApiModule
import com.android.github.domain.model.Repository
import com.android.github.domain.usecase.GithubUseCase
import com.android.github.utils.AuthPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepositoriesViewModel(
    private val useCase: GithubUseCase = GithubUseCase(repository = ApiModule.repository),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _repositories =
        MutableStateFlow<RepositoriesResult<List<Repository>>>(RepositoriesResult.Loading)
    val repositories = _repositories.asStateFlow()

    fun loadRepositories(context: Context) {
        viewModelScope.launch(dispatcher) {
            val token = AuthPreferences.getAccessToken(context)
            try {
                useCase.getAuthenticatedUserRepos(token ?: "").onSuccess { repos ->
                    _repositories.value = RepositoriesResult.Success(repos)

                }
            } catch (e: Exception) {
                _repositories.value = RepositoriesResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class RepositoriesResult<out T> {
    data object Loading : RepositoriesResult<Nothing>()
    data class Success<T>(val data: T) : RepositoriesResult<T>()
    data class Error(val message: String) : RepositoriesResult<Nothing>()
}