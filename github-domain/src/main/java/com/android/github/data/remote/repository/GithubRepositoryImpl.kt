package com.android.github.data.remote.repository

import com.android.github.common.clientId
import com.android.github.common.clientSecret
import com.android.github.data.remote.api.GithubApi
import com.android.github.data.remote.api.GithubAuthApi
import com.android.github.data.remote.mapper.toDomain
import com.android.github.data.remote.mapper.toDto
import com.android.github.domain.model.AuthUser
import com.android.github.domain.model.CreateIssueRequest
import com.android.github.domain.model.Issue
import com.android.github.domain.model.Repository
import com.android.github.domain.model.SearchResult
import com.android.github.domain.model.User
import com.android.github.domain.repository.GithubRepository
import java.time.LocalDate

class GithubRepositoryImpl(
    private val api: GithubApi,
    private val authApi: GithubAuthApi,
) : GithubRepository {

    override suspend fun getAuthenticatedUserRepos(token: String): Result<List<Repository>> = runCatching {
        api.getAuthenticatedUserRepos("Bearer $token").map { it.toDomain() }
    }

    override suspend fun getPopularRepos(
        language: String?,
        since: String
    ): Result<List<Repository>> = runCatching {
        val query = buildString {
            append("stars:>1")
            language?.let { append(" language:$it") }
            when (since) {
                "weekly" -> append(" created:>${LocalDate.now().minusWeeks(1)}")
                "monthly" -> append(" created:>${LocalDate.now().minusMonths(1)}")
                else -> append(" created:>${LocalDate.now().minusDays(1)}")
            }
        }
        api.searchRepositories(query).items.map { it.toDomain() }
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

    override suspend fun createIssue(
        owner: String,
        repo: String,
        issue: CreateIssueRequest
    ): Result<Issue> = runCatching {
        api.createIssue(owner, repo, issue.toDto()).toDomain()
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

}