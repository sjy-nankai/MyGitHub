package com.android.mygithub.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.github.data.remote.di.ApiModule
import com.android.github.domain.model.AuthState
import com.android.github.domain.model.User
import com.android.github.domain.usecase.GithubUseCase
import com.android.github.utils.AuthPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val authState: AuthState? = null,
    val isError: Boolean = false,
)

class ProfileViewModel(
    private val useCase: GithubUseCase = GithubUseCase(repository = ApiModule.repository),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private var _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState>
        get() = _uiState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("my-github", exception.toString())
    }

    fun getAuthState(context: Context) {
        val token = AuthPreferences.getAccessToken(context)
        val code = AuthPreferences.getAccessCode(context)
        viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
            if (token !== null) {
                useCase.getAuthenticatedUser(token).onSuccess { user ->
                    updateUserState(user, token)
                }
            } else if (code != null) {
                useCase.login(code).onSuccess { authUser ->
                    AuthPreferences.saveAccessToken(context, authUser.token.accessToken)
                    updateUserState(authUser.user, authUser.token.accessToken)
                }.onFailure {
                    _uiState.update { state ->
                        state.copy(
                            isError = true
                        )
                    }
                }

            } else {
                _uiState.update { state ->
                    state.copy(
                        authState = AuthState.Anonymous
                    )
                }
            }
        }
    }

    private fun updateUserState(user: User, token: String) {
        _uiState.update { state ->
            state.copy(
                authState = AuthState.Authenticated(
                    User(
                        username = user.username,
                        name = user.name,
                        avatarUrl = user.avatarUrl,
                        bio = user.bio,
                        repoCount = user.repoCount,
                        followers = user.followers,
                        following = user.following,
                    ), token
                )
            )
        }
    }


    fun signOut(context: Context) {
        viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
            AuthPreferences.clearAuth(context)
            _uiState.update { state ->
                state.copy(
                    authState = AuthState.Anonymous
                )
            }
        }
    }
}