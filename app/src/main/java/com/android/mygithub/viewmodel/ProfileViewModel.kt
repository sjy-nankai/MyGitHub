package com.android.mygithub.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.github.data.remote.di.AppModule
import com.android.github.domain.model.AuthState
import com.android.github.domain.model.User
import com.android.github.domain.usecase.GithubUseCase
import com.android.github.utils.AuthPreferences
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val authState: AuthState? = null,
    val isError: Boolean = false,
)

class ProfileViewModel(
    private val useCase: GithubUseCase = GithubUseCase(repository = AppModule.repository)
) : ViewModel() {

    private var _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState>
        get() = _uiState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("my-github", exception.toString())
    }

    fun getAuthState(context: Context) {

        viewModelScope.launch(coroutineExceptionHandler) {
            AuthPreferences.code.let {
                if (it.isNotEmpty()) {
                    useCase.login(it).onSuccess { authUser ->
//                        AuthPreferences.removeAccessCode(context)
                        _uiState.update { state ->
                            state.copy(
                                authState = AuthState.Authenticated(
                                    User(
                                        username = authUser.user.username,
                                        name = authUser.user.name,
                                        avatarUrl = authUser.user.avatarUrl,
                                        bio = authUser.user.bio,
                                        repoCount = authUser.user.repoCount,
                                        followers = authUser.user.followers,
                                        following = authUser.user.following,
                                    ), authUser.token.accessToken
                                )
                            )
                        }
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
    }


    fun signIn() {
        viewModelScope.launch {
            AuthPreferences.code = ""
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    authState = AuthState.Anonymous
                )
            }
        }
    }
}