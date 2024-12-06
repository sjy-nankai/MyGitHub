package com.android.github.data.remote.api

import com.android.github.data.remote.dto.CreateIssueRequestDto
import com.android.github.data.remote.dto.IssueDto
import com.android.github.data.remote.dto.RepositoryDto
import com.android.github.data.remote.dto.SearchResultDto
import com.android.github.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30,
    ): SearchResultDto

    @GET("user")
    suspend fun getAuthenticatedUser(
        @Header("Authorization") authHeader: String?
    ): UserDto

    @GET("user/repos")
    suspend fun getAuthenticatedUserRepos(
        @Header("Authorization") authHeader: String?,
        @Query("sort") sort: String = "updated",
        @Query("per_page") perPage: Int = 30
    ): List<RepositoryDto>

    @POST("repos/{owner}/{repo}/issues")
    suspend fun createIssue(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body issue: CreateIssueRequestDto
    ): IssueDto
}