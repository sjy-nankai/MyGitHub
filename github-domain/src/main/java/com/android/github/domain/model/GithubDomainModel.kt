package com.android.github.domain.model

data class User(
    val username: String,
    val name: String?,
    val avatarUrl: String,
    val bio: String?,
    val repoCount: Int,
    val followers: Int,
    val following: Int
)

data class SearchResult(
    val items: List<Repository>,
    val totalCount: Int,
    val hasNextPage: Boolean
)

data class Repository(
    val id: Int,
    val name: String,
    val fullName: String,
    val description: String?,
    val language: String?,
    val starCount: Int,
    val forkCount: Int,
    val owner: RepositoryOwner,
    val updatedAt: String
)

data class RepositoryOwner(
    val id: Int,
    val login: String,
    val avatarUrl: String
)

sealed class AuthState {
    data object Anonymous : AuthState()
    data class Authenticated(val user: User, val token: String) : AuthState()
}

data class Issue(
    val id: Int,
    val number: Int,
    val title: String,
    val body: String,
    val state: String,
    val createdAt: String,
    val user: User
)

data class CreateIssueRequest(
    val title: String,
    val body: String,
    val labels: List<String> = emptyList()
)

data class AuthToken(
    val accessToken: String,
    val tokenType: String,
    val scope: String
)

data class AuthUser(
    val token: AuthToken,
    val user: User
)
