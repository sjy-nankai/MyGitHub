package com.android.github.data.remote.api

import com.android.github.data.remote.dto.AuthTokenDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GithubAuthApi {

    @GET("login/oauth/access_token")
    suspend fun getAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String,
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
    ): AuthTokenDto


}