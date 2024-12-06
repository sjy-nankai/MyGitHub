package com.android.mygithub

import com.android.github.domain.usecase.GithubUseCase
import com.android.mygithub.mock.mockErrorMessage
import com.android.mygithub.mock.mockRepos
import com.android.mygithub.viewmodel.PopularViewModel
import io.kotest.core.coroutines.coroutineTestScope
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain


@OptIn(ExperimentalCoroutinesApi::class, ExperimentalStdlibApi::class)
class PopularViewModelTest : FunSpec({
    val scope = TestScope()
    val testDispatcher: CoroutineDispatcher = StandardTestDispatcher(scope.testScheduler)
    lateinit var useCase: GithubUseCase
    lateinit var viewModel: PopularViewModel
    beforeTest {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk(relaxed = true)
        viewModel = PopularViewModel(
            useCase = useCase,
            dispatcher = testDispatcher,
        )
    }

    afterTest {
        Dispatchers.resetMain()
        testDispatcher.cancelChildren()
        unmockkAll()
        clearAllMocks()
    }

    test("loadPopularRepos test success").config(
        coroutineTestScope = true
    ) {
        coEvery {
            useCase.getPopularRepos(
                any(),
                any()
            )
        } returns Result.success(mockRepos)
        viewModel.loadPopularRepos()
        testCoroutineScheduler.advanceUntilIdle()
        with(viewModel.uiState.value) {
            data.shouldNotBeEmpty()
            data.size shouldBe 2
            isError.shouldBeFalse()
        }
    }

    test("loadPopularRepos should show loading state while fetching").config(
        coroutineTestScope = true
    ) {
        coEvery { useCase.getPopularRepos(any(), any()) } returns Result.success(emptyList())
        viewModel.loadPopularRepos()
        with(viewModel.uiState.value) {
            isRefreshing.shouldBeTrue()
        }
        coroutineTestScope.advanceUntilIdle()
        with(viewModel.uiState.value) {
            isRefreshing.shouldBeFalse()
        }
    }

    test("loadPopularRepos network failure").config(
        coroutineTestScope = true
    ) {
        coEvery {
            useCase.getPopularRepos(
                any(),
                any()
            )
        } returns Result.failure(Exception(mockErrorMessage))
        viewModel.loadPopularRepos()
        testCoroutineScheduler.advanceUntilIdle()
        viewModel.uiState.value.isError.shouldBeTrue()
    }
})