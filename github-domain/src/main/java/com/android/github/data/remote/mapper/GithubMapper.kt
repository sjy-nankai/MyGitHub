package com.android.github.data.remote.mapper

import com.android.github.data.remote.dto.AuthTokenDto
import com.android.github.data.remote.dto.CreateIssueRequestDto
import com.android.github.data.remote.dto.IssueDto
import com.android.github.data.remote.dto.PopularRepoDto
import com.android.github.data.remote.dto.RepoDto
import com.android.github.data.remote.dto.RepoOwnerDto
import com.android.github.data.remote.dto.RepositoryDto
import com.android.github.data.remote.dto.RepositoryOwnerDto
import com.android.github.data.remote.dto.SearchResponseDto
import com.android.github.data.remote.dto.UserDto
import com.android.github.domain.model.AuthToken
import com.android.github.domain.model.CreateIssueRequest
import com.android.github.domain.model.Issue
import com.android.github.domain.model.PopularRepo
import com.android.github.domain.model.Repo
import com.android.github.domain.model.RepoOwner
import com.android.github.domain.model.Repository
import com.android.github.domain.model.RepositoryOwner
import com.android.github.domain.model.SearchResult
import com.android.github.domain.model.User

fun UserDto.toDomain() = User(
    username = login,
    name = name,
    avatarUrl = avatar_url,
    bio = bio,
    repoCount = public_repos,
    followers = followers,
    following = following
)

fun RepoDto.toDomain() = Repo(
    name = name,
    fullName = full_name,
    description = description,
    stars = stargazers_count,
    forks = forks_count,
    language = language
)

fun PopularRepoDto.toDomain() = PopularRepo(
    name = name,
    fullName = full_name,
    description = description,
    stars = stargazers_count,
    forks = forks_count,
    language = language,
    owner = owner.toDomain()
)

fun RepoOwnerDto.toDomain() = RepoOwner(
    login = login,
    avatarUrl = avatar_url
)

fun AuthTokenDto.toDomain() = AuthToken(
    accessToken = access_token,
    tokenType = token_type,
    scope = scope
)

fun SearchResponseDto.toDomain(currentPage: Int, perPage: Int) = SearchResult(
    items = items.map { it.toDomain() },
    totalCount = total_count,
    hasNextPage = items.size == perPage && (currentPage * perPage) < total_count
)

fun RepositoryDto.toDomain() = Repository(
    id = id,
    name = name,
    fullName = full_name,
    description = description,
    language = language,
    starCount = stargazers_count,
    forkCount = forks_count,
    owner = owner.toDomain(),
    updatedAt = updated_at
)

fun RepositoryOwnerDto.toDomain() = RepositoryOwner(
    id = id,
    login = login,
    avatarUrl = avatar_url
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