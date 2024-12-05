package com.android.github.domain.repository

import com.android.github.domain.model.AuthUser
import com.android.github.domain.model.CreateIssueRequest
import com.android.github.domain.model.Issue
import com.android.github.domain.model.Repository
import com.android.github.domain.model.SearchResult
import com.android.github.domain.model.User

interface GithubRepository {

    suspend fun getAuthenticatedUserRepos(username: String): Result<List<Repository>>

    suspend fun getPopularRepos(
        language: String? = null,
        since: String = "weekly"
    ): Result<List<Repository>>

    suspend fun searchRepositories(
        language: String?,
        query: String = "",
        sort: String = "stars",
        order: String = "desc",
        page: Int = 1,
        perPage: Int = 30
    ): Result<SearchResult>

    suspend fun getAuthenticatedUser(token: String?): Result<User>

    suspend fun createIssue(owner: String, repo: String, issue: CreateIssueRequest): Result<Issue>

    suspend fun login(code: String): Result<AuthUser>

    suspend fun logout()

}