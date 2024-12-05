package com.android.github.data.remote.api

import com.android.github.data.remote.dto.AuthTokenDto
import com.android.github.data.remote.dto.CreateIssueRequestDto
import com.android.github.data.remote.dto.IssueDto
import com.android.github.data.remote.dto.RepoDto
import com.android.github.data.remote.dto.RepositoryDto
import com.android.github.data.remote.dto.SearchRepoResponse
import com.android.github.data.remote.dto.SearchResponseDto
import com.android.github.data.remote.dto.UserDto
import retrofit2.http.*

interface GithubApi {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): UserDto

    @GET("search/repositories")
    suspend fun getPopularRepos(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 30
    ): SearchRepoResponse

    @GET("login/oauth/access_token")
    suspend fun getAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String,
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
    ): AuthTokenDto

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchResponseDto

    @GET("user")
    suspend fun getAuthenticatedUser(
        @Header("Authorization") authHeader: String?
    ): UserDto

    @GET("user/repos")
    suspend fun getAuthenticatedUserRepos(
        @Header("Authorization") authHeader: String?,
        @Query("sort") sort: String = "updated",  // 可选：按最近更新排序
        @Query("per_page") perPage: Int = 30
    ): List<RepoDto>

    @GET("user")
    suspend fun getUserProfile(
        @Header("Authorization") authHeader: String?
    ): UserDto

    @GET("user/repos")
    suspend fun getUserRepos(
        @Header("Authorization") authHeader: String?
    ): List<RepoDto>


    @GET("user/repos")
    suspend fun getUserRepositories(): List<RepositoryDto>

    @POST("repos/{owner}/{repo}/issues")
    suspend fun createIssue(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body issue: CreateIssueRequestDto
    ): IssueDto
}