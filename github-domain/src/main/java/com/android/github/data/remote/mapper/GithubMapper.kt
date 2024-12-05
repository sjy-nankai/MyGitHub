package com.android.github.data.remote.mapper

import com.android.github.data.remote.dto.AuthTokenDto
import com.android.github.data.remote.dto.CreateIssueRequestDto
import com.android.github.data.remote.dto.IssueDto
import com.android.github.data.remote.dto.RepositoryDto
import com.android.github.data.remote.dto.RepositoryOwnerDto
import com.android.github.data.remote.dto.SearchResultDto
import com.android.github.data.remote.dto.UserDto
import com.android.github.domain.model.AuthToken
import com.android.github.domain.model.CreateIssueRequest
import com.android.github.domain.model.Issue
import com.android.github.domain.model.Repository
import com.android.github.domain.model.RepositoryOwner
import com.android.github.domain.model.SearchResult
import com.android.github.domain.model.User

fun UserDto.toDomain() = User(
    username = login,
    name = name,
    avatarUrl = avatarUrl,
    bio = bio,
    repoCount = publicRepos,
    followers = followers,
    following = following
)


fun AuthTokenDto.toDomain() = AuthToken(
    accessToken = accessToken,
    tokenType = tokenType,
    scope = scope
)

fun SearchResultDto.toDomain(currentPage: Int, perPage: Int) = SearchResult(
    items = items.map { it.toDomain() },
    totalCount = totalCount,
    hasNextPage = items.size == perPage && (currentPage * perPage) < totalCount
)

fun RepositoryDto.toDomain() = Repository(
    id = id,
    name = name,
    fullName = fullName,
    description = description,
    language = language,
    starCount = stargazersCount,
    forkCount = forksCount,
    owner = owner.toDomain(),
    updatedAt = updatedAt
)

fun RepositoryOwnerDto.toDomain() = RepositoryOwner(
    id = id,
    login = login,
    avatarUrl = avatarUrl
)

fun IssueDto.toDomain() = Issue(
    id = id,
    number = number,
    title = title,
    body = body,
    state = state,
    createdAt = created_at,
    user = user.toDomain()
)

fun CreateIssueRequest.toDto() = CreateIssueRequestDto(
    title = title,
    body = body,
    labelNames = labels
)