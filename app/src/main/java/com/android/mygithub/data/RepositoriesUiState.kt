package com.android.mygithub.data

sealed class RepositoriesUiState<out T> {
    data object Loading : RepositoriesUiState<Nothing>()
    data class Success<T>(val data: T) : RepositoriesUiState<T>()
    data class Error(val message: String) : RepositoriesUiState<Nothing>()
}