package com.android.github.data.remote.repository

import android.content.Context
import com.android.github.common.clientId
import com.android.github.common.clientSecret
import com.android.github.data.remote.api.GithubApi
import com.android.github.data.remote.api.GithubAuthApi
import com.android.github.data.remote.mapper.toDomain
import com.android.github.data.remote.mapper.toDto
import com.android.github.domain.model.AuthState
import com.android.github.domain.model.AuthToken
import com.android.github.domain.model.AuthUser
import com.android.github.domain.model.CreateIssueRequest
import com.android.github.domain.model.Issue
import com.android.github.domain.model.PopularRepo
import com.android.github.domain.model.Repo
import com.android.github.domain.model.SearchResult
import com.android.github.domain.model.User
import com.android.github.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class GithubRepositoryImpl(
    private val api: GithubApi,
    private val authApi: GithubAuthApi,
) : GithubRepository {

    override suspend fun getUser(username: String): Result<User> = runCatching {
        api.getUser(username).toDomain()
    }

    override suspend fun getUserRepos(username: String): Result<List<Repo>> = runCatching {
        api.getUserRepos(username).map { it.toDomain() }
    }

    override suspend fun getPopularRepos(
        language: String?,
        since: String
    ): Result<List<PopularRepo>> = runCatching {
        val query = buildString {
            append("stars:>1")
            language?.let { append(" language:$it") }
            when (since) {
                "weekly" -> append(" created:>${LocalDate.now().minusWeeks(1)}")
                "monthly" -> append(" created:>${LocalDate.now().minusMonths(1)}")
                else -> append(" created:>${LocalDate.now().minusDays(1)}")
            }
        }
        api.getPopularRepos(query).items.map { it.toDomain() }
    }

    override suspend fun searchRepositories(
        language: String?,
        query: String,
        sort: String,
        order: String,
        page: Int,
        perPage: Int
    ): Result<SearchResult> = runCatching {
        val searchQuery = buildString {
            if (query.isNotBlank()) {
                append(query)
                append(" ")
            }
            if (language != null) {
                append("language:$language ")
            }
            if (isEmpty()) {
                append("stars:>1")
            }
        }

        api.searchRepositories(
            query = searchQuery.trim(),
            sort = sort,
            order = order,
            page = page,
            perPage = perPage
        ).toDomain(page, perPage)
    }

    override suspend fun getAuthenticatedUser(token: String?): Result<User> = runCatching {
        api.getAuthenticatedUser("Bearer $token").toDomain()
    }


    override suspend fun getAuthState(token: String?): AuthState {
        return if (token != null) {
            val user = api.getAuthenticatedUser(token).toDomain()
            AuthState.Authenticated(user, token)
        } else {
            AuthState.Anonymous
        }
    }

    override suspend fun signOut() {
        // TODO: sjy
    }

    override suspend fun createIssue(
        owner: String,
        repo: String,
        issue: CreateIssueRequest
    ): Result<Issue> = runCatching {
        api.createIssue(owner, repo, issue.toDto()).toDomain()
    }

    override suspend fun getAuthStateFlow(token: String?): Flow<AuthState> {
        return flow {
            if (token != null) {
                try {
                    val user = api.getAuthenticatedUser(token).toDomain()
                    AuthState.Authenticated(user, token)
                } catch (e: Exception) {
                    AuthState.Anonymous
                }
            } else {
                AuthState.Anonymous
            }
        }
    }

    override suspend fun login(code: String): Result<AuthUser> = runCatching {
        val tokenDto = authApi.getAccessToken(
            clientId = clientId,
            clientSecret = clientSecret,
            code = code
        )
        val token = tokenDto.toDomain()
        val user = api.getAuthenticatedUser("${token.tokenType} ${token.accessToken}").toDomain()
        AuthUser(token, user)
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun getStoredToken(): AuthToken? {
        TODO("Not yet implemented")
    }

    override suspend fun saveToken(token: AuthToken) {
        TODO("Not yet implemented")
    }

    override suspend fun clearToken() {
        TODO("Not yet implemented")
    }
}