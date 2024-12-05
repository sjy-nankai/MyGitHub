package com.android.github.domain.repository

import android.content.Context
import com.android.github.domain.model.AuthState
import com.android.github.domain.model.AuthToken
import com.android.github.domain.model.AuthUser
import com.android.github.domain.model.CreateIssueRequest
import com.android.github.domain.model.Issue
import com.android.github.domain.model.PopularRepo
import com.android.github.domain.model.Repo
import com.android.github.domain.model.SearchResult
import com.android.github.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GithubRepository {

    suspend fun getUser(username: String): Result<User>
    suspend fun getUserRepos(username: String): Result<List<Repo>>
    suspend fun getPopularRepos(
        language: String? = null,
        since: String = "daily"
    ): Result<List<PopularRepo>>

    suspend fun searchRepositories(
        language: String?,
        query: String = "",
        sort: String = "stars",
        order: String = "desc",
        page: Int = 1,
        perPage: Int = 30
    ): Result<SearchResult>

    suspend fun getAuthenticatedUser(token: String?): Result<User>

    suspend fun getAuthState(token: String?): AuthState
    suspend fun signOut()
    suspend fun createIssue(owner: String, repo: String, issue: CreateIssueRequest): Result<Issue>
    suspend fun getAuthStateFlow(token: String?): Flow<AuthState>

    suspend fun login(code: String): Result<AuthUser>
    suspend fun logout()
    suspend fun getStoredToken(): AuthToken?
    suspend fun saveToken(token: AuthToken)
    suspend fun clearToken()

}