package com.android.github.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    val login: String,
    val avatar_url: String,
    val name: String?,
    val bio: String?,
    val public_repos: Int,
    val followers: Int,
    val following: Int
)

data class RepoDto(
    val name: String,
    val full_name: String,
    val description: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val language: String?
)

data class SearchRepoResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<PopularRepoDto>
)

data class PopularRepoDto(
    val name: String,
    val full_name: String,
    val description: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val language: String?,
    val owner: RepoOwnerDto
)

data class RepoOwnerDto(
    val login: String,
    val avatar_url: String
)

data class AuthTokenDto(
    val access_token: String,
    val token_type: String,
    val scope: String
)

data class SearchResponseDto(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<RepositoryDto>
)

data class RepositoryDto(
    val id: Int,
    val name: String,
    val full_name: String,
    val description: String?,
    val language: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val owner: RepositoryOwnerDto,
    val updated_at: String
)

data class RepositoryOwnerDto(
    val id: Int,
    val login: String,
    val avatar_url: String
)

data class IssueDto(
    val id: Int,
    val number: Int,
    val title: String,
    val body: String,
    val state: String,
    val created_at: String,
    val user: UserDto,
    val labels: List<LabelDto>,
    val comments: Int,
    val html_url: String
)

data class LabelDto(
    val id: Long,
    val name: String,
    val color: String,
    val description: String?
)


data class CreateIssueRequestDto(
    val title: String,
    val body: String,

    @SerializedName("labels")
    val labelNames: List<String>,

    // 可选字段
    val assignees: List<String>? = null,
    val milestone: Int? = null
)
