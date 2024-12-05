package com.android.mygithub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.github.data.remote.di.AppModule
import com.android.github.domain.model.PopularRepo
import com.android.github.domain.usecase.GithubUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val isRefreshing: Boolean = false,
    val isError: Boolean = false,
    val data: List<PopularRepo>? = null,
)

class MainViewModel(
    private val useCase: GithubUseCase = GithubUseCase(repository = AppModule.repository)
) : ViewModel() {

    private var _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState>
        get() = _uiState

    fun loadPopularRepos(language: String? = null, since: String = "daily") {
        _uiState.update {
            it.copy(
                isRefreshing = true
            )
        }
        viewModelScope.launch {
            useCase.getPopularRepos(language, since)
                .onSuccess { repos ->
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            isError = false,
                            data = repos
                        )
                    }
                }
                .onFailure { error ->
                    println(error)
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            isError = true,
                        )
                    }
                }
        }
    }
}