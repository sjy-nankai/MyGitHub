package com.android.mygithub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.github.data.remote.di.ApiModule
import com.android.github.domain.model.Repository
import com.android.github.domain.usecase.GithubUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchState(
    val query: String = "",
    val selectedLanguage: String? = "default",
    val isLoading: Boolean = false,
    val repositories: List<Repository> = emptyList(),
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
    val error: String? = null
)

class SearchViewModel(
    private val useCase: GithubUseCase = GithubUseCase(repository = ApiModule.repository),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var searchJob: Job? = null
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("my-github", exception.toString())
    }

    fun onQueryChange(query: String) {
        _state.value = _state.value.copy(query = query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // 防抖
            search(resetPage = true)
        }
    }

    fun onLanguageSelect(language: String?) {
        _state.value = _state.value.copy(selectedLanguage = language)
        search(resetPage = true)
    }

    fun loadNextPage() {
        if (_state.value.isLoading || !_state.value.hasNextPage) return
        search(resetPage = false)
    }

    private fun search(resetPage: Boolean) {
        viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
            val currentState = _state.value
            val page = if (resetPage) 1 else currentState.currentPage + 1

            _state.value = currentState.copy(
                isLoading = true,
                error = null
            )

            useCase.searchRepos(
                language = currentState.selectedLanguage,
                query = currentState.query,
                page = page
            ).onSuccess { result ->
                _state.value = currentState.copy(
                    repositories = if (resetPage) result.items else currentState.repositories + result.items,
                    currentPage = page,
                    hasNextPage = result.hasNextPage,
                    isLoading = false
                )
            }.onFailure { error ->
                _state.value = currentState.copy(
                    error = error.message,
                    isLoading = false
                )
            }
        }
    }
}