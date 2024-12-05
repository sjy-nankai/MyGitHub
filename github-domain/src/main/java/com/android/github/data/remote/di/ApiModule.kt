package com.android.github.data.remote.di

import com.android.github.data.remote.api.GithubApi
import com.android.github.data.remote.api.GithubAuthApi
import com.android.github.data.remote.repository.GithubRepositoryImpl
import com.android.github.domain.repository.GithubRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {

    private const val BASE_URL = "https://api.github.com"
    private const val AUTH_URL = "https://github.com"
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/vnd.github+json")
                .build()
            chain.proceed(request)
        }
        .build()

    private val authRetrofit = Retrofit.Builder()
        .baseUrl(AUTH_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api: GithubApi = retrofit.create(GithubApi::class.java)

    private val authApi: GithubAuthApi = authRetrofit.create(GithubAuthApi::class.java)

    val repository: GithubRepository =
        GithubRepositoryImpl(
            api = api,
            authApi = authApi,
        )
}