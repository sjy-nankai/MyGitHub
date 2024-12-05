package com.android.github.domain.usecase

import com.android.github.domain.model.AuthUser
import com.android.github.domain.model.PopularRepo
import com.android.github.domain.model.Repo
import com.android.github.domain.model.SearchResult
import com.android.github.domain.model.User
import com.android.github.domain.repository.GithubRepository

class GithubUseCase(private val repository: GithubRepository) {
    suspend fun getUser(username: String): Result<User> =
        repository.getUser(username)

    suspend fun getUserRepos(username: String): Result<List<Repo>> =
        repository.getUserRepos(username)

    suspend fun getPopularRepos(
        language: String? = null,
        since: String = "daily"
    ): Result<List<PopularRepo>> =
        repository.getPopularRepos(language, since)


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