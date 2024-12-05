package com.android.github.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("login")
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("public_repos")
    val publicRepos: Int,
    @SerializedName("followers")
    val followers: Int,
    @SerializedName("following")
    val following: Int
)

data class AuthTokenDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("scope")
    val scope: String
)

data class SearchResultDto(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val items: List<RepositoryDto>
)

data class RepositoryDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    @SerializedName("forks_count")
    val forksCount: Int,
    @SerializedName("owner")
    val owner: RepositoryOwnerDto,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class RepositoryOwnerDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
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
