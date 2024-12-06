package com.android.mygithub

import android.content.Context
import com.android.github.domain.model.AuthState
import com.android.github.domain.model.User
import com.android.github.domain.usecase.GithubUseCase
import com.android.github.utils.AuthPreferences
import com.android.mygithub.mock.mockToken
import com.android.mygithub.viewmodel.ProfileViewModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain


@OptIn(ExperimentalCoroutinesApi::class, ExperimentalStdlibApi::class)
class ProfileViewModelTest : FunSpec({
    val scope = TestScope()
    val testDispatcher: CoroutineDispatcher = StandardTestDispatcher(scope.testScheduler)
    lateinit var useCase: GithubUseCase
    lateinit var viewModel: ProfileViewModel
    lateinit var context: Context
    beforeTest {
        Dispatchers.setMain(testDispatcher)
        context = mockk()
        useCase = mockk(relaxed = true)
        viewModel = ProfileViewModel(
            useCase = useCase,
            dispatcher = testDispatcher,
        )
        mockkObject(AuthPreferences)
    }

    afterTest {
        Dispatchers.resetMain()
        testDispatcher.cancelChildren()
        unmockkAll()
        clearAllMocks()
    }

    test("getAuthState should handle authenticated user with token").config(
        coroutineTestScope = true
    ) {
        val mockUser = User(
            username = "testUser",
            name = "Test User",
            avatarUrl = "https://test.com/avatar",
            bio = "Test bio",
            repoCount = 10,
            followers = 100,
            following = 50
        )

        every { AuthPreferences.getAccessToken(context) } returns mockToken
        every { AuthPreferences.getAccessCode(context) } returns null
        coEvery { useCase.getAuthenticatedUser(mockToken) } returns Result.success(mockUser)

        viewModel.getAuthState(context)
        testCoroutineScheduler.advanceUntilIdle()

        val authState = viewModel.uiState.value.authState
        authState.shouldBeInstanceOf<AuthState.Authenticated>()
        with(authState) {
            user.username shouldBe mockUser.username
            user.name shouldBe mockUser.name
            user.avatarUrl shouldBe mockUser.avatarUrl
            user.bio shouldBe mockUser.bio
            user.repoCount shouldBe mockUser.repoCount
            user.followers shouldBe mockUser.followers
            user.following shouldBe mockUser.following
            token shouldBe token
        }
    }

    test("getAuthState should handle login failure").config(coroutineTestScope = true) {
        val code = "test-code"
        every { AuthPreferences.getAccessToken(context) } returns null
        every { AuthPreferences.getAccessCode(context) } returns code
        coEvery { useCase.login(code) } returns Result.failure(Exception("Login failed"))

        viewModel.getAuthState(context)
        testCoroutineScheduler.advanceUntilIdle()
        viewModel.uiState.value.isError shouldBe true
    }

    test("getAuthState should handle anonymous state").config(coroutineTestScope = true) {
        every { AuthPreferences.getAccessToken(context) } returns null
        every { AuthPreferences.getAccessCode(context) } returns null
        viewModel.getAuthState(context)
        testCoroutineScheduler.advanceUntilIdle()

        viewModel.uiState.value.authState shouldBe AuthState.Anonymous
    }

    test("signOut should clear auth and set anonymous state").config(coroutineTestScope = true) {
        coEvery { AuthPreferences.clearAuth(context) } just Runs
        viewModel.signOut(context)
        testCoroutineScheduler.advanceUntilIdle()
        viewModel.uiState.value.authState shouldBe AuthState.Anonymous
        coVerify { AuthPreferences.clearAuth(context) }
    }

})