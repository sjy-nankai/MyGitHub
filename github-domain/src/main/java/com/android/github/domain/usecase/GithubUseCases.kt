package com.android.github.domain.usecase

import com.android.github.domain.model.AuthUser
import com.android.github.domain.model.Repository
import com.android.github.domain.model.SearchResult
import com.android.github.domain.model.User
import com.android.github.domain.repository.GithubRepository

class GithubUseCase(private val repository: GithubRepository) {

    suspend fun getAuthenticatedUserRepos(username: String): Result<List<Repository>> =
        repository.getAuthenticatedUserRepos(username)

    suspend fun getPopularRepos(
        language: String? = null,
        since: String = "daily"
    ): Result<List<Repository>> =
        repository.getPopularRepos(language, since)

    suspend fun getAuthenticatedUser(
        token: String
    ): Result<User> =
        repository.getAuthenticatedUser(token)


    suspend fun login(code: String): Result<AuthUser> =
        repository.login(code)

    suspend fun searchRepos(
        language: String?,
        query: String = "",
        sort: String = "stars",
        order: String = "desc",
        page: Int = 1,
        perPage: Int = 30
    ): Result<SearchResult> = repository.searchRepositories(
        language = language,
        query = query,
        sort = sort,
        order = order,
        page = page,
        perPage = perPage
    )
}