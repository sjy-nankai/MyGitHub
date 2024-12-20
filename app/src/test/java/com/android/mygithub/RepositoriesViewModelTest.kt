package com.android.mygithub

import android.content.Context
import com.android.github.domain.model.Repository
import com.android.github.domain.usecase.GithubUseCase
import com.android.github.utils.AuthPreferences
import com.android.mygithub.data.RepositoriesUiState
import com.android.mygithub.mock.mockErrorMessage
import com.android.mygithub.mock.mockRepos
import com.android.mygithub.mock.mockToken
import com.android.mygithub.viewmodel.RepositoriesViewModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
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
class RepositoriesViewModelTest : FunSpec({
    val scope = TestScope()
    val testDispatcher: CoroutineDispatcher = StandardTestDispatcher(scope.testScheduler)
    lateinit var useCase: GithubUseCase
    lateinit var context: Context
    lateinit var viewModel: RepositoriesViewModel
    beforeTest {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk(relaxed = true)
        context = mockk()
        viewModel = RepositoriesViewModel(
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

    test("initial state should be Loading") {
        viewModel.uiState.value.shouldBeInstanceOf<RepositoriesUiState.Loading>()
    }

    test("loadRepositories should handle successful response").config(coroutineTestScope = true) {
        val mockRepos = mockRepos

        every { AuthPreferences.getAccessToken(context) } returns mockToken
        coEvery { useCase.getAuthenticatedUserRepos(mockToken) } returns Result.success(mockRepos)

        viewModel.loadRepositories(context)
        testCoroutineScheduler.advanceUntilIdle()

        val result = viewModel.uiState.value
        result.shouldBeInstanceOf<RepositoriesUiState.Success<List<Repository>>>()
        result.data shouldBe mockRepos
    }

    test("loadRepositories should handle null token").config(coroutineTestScope = true) {
        every { AuthPreferences.getAccessToken(context) } returns null
        coEvery { useCase.getAuthenticatedUserRepos("") } returns Result.success(emptyList())
        viewModel.loadRepositories(context)
        testCoroutineScheduler.advanceUntilIdle()
        val result = viewModel.uiState.value
        result.shouldBeInstanceOf<RepositoriesUiState.Success<List<Repository>>>()
        result.data shouldBe emptyList()
    }

    test("loadRepositories should handle error response").config(coroutineTestScope = true) {

        every { AuthPreferences.getAccessToken(context) } returns mockToken
        coEvery {
            useCase.getAuthenticatedUserRepos(mockToken)
        } returns Result.failure(Exception(mockErrorMessage))

        viewModel.loadRepositories(context)
        testCoroutineScheduler.advanceUntilIdle()

        val result = viewModel.uiState.value
        result.shouldBeInstanceOf<RepositoriesUiState.Error>()
    }

    test("loadRepositories should handle exception").config(coroutineTestScope = true) {

        every { AuthPreferences.getAccessToken(context) } returns mockToken
        coEvery {
            useCase.getAuthenticatedUserRepos(mockToken)
        } throws Exception(mockErrorMessage)

        viewModel.loadRepositories(context)
        testCoroutineScheduler.advanceUntilIdle()

        val result = viewModel.uiState.value
        result.shouldBeInstanceOf<RepositoriesUiState.Error>()
        result.message shouldBe mockErrorMessage
    }
})