package com.android.mygithub

import com.android.github.domain.model.SearchResult
import com.android.github.domain.usecase.GithubUseCase
import com.android.mygithub.mock.mockErrorMessage
import com.android.mygithub.mock.mockRepos
import com.android.mygithub.viewmodel.SearchViewModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.testCoroutineScheduler
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
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain


@OptIn(ExperimentalCoroutinesApi::class, ExperimentalStdlibApi::class)
class SearchViewModelTest : FunSpec({
    val scope = TestScope()
    val testDispatcher: CoroutineDispatcher = StandardTestDispatcher(scope.testScheduler)
    lateinit var useCase: GithubUseCase
    lateinit var viewModel: SearchViewModel
    beforeTest {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk(relaxed = true)
        viewModel = SearchViewModel(
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

    test("onQueryChange should trigger search after debounce delay").config(
        coroutineTestScope = true
    ) {
        val searchResult = SearchResult(
            items = mockRepos,
            hasNextPage = false,
            totalCount = 2,
        )
        coEvery {
            useCase.searchRepos(
                language = any(),
                query = "test",
                page = 1
            )
        } returns Result.success(searchResult)
        viewModel.onQueryChange("test")
        viewModel.uiState.value.repositories shouldBe emptyList()
        testCoroutineScheduler.advanceUntilIdle()
        with(viewModel.uiState.value) {
            query shouldBe "test"
            repositories shouldBe searchResult.items
            currentPage shouldBe 1
            hasNextPage shouldBe false
            error shouldBe null
            isLoading shouldBe false
        }
    }

    test("onLanguageSelect should trigger immediate search").config(
        coroutineTestScope = true
    ) {
        val searchResult = SearchResult(
            items = mockRepos,
            hasNextPage = true,
            totalCount = 2
        )
        coEvery {
            useCase.searchRepos(
                language = "kotlin",
                query = "",
                page = 1
            )
        } returns Result.success(searchResult)

        viewModel.onLanguageSelect("kotlin")
        testCoroutineScheduler.advanceUntilIdle()

        with(viewModel.uiState.value) {
            selectedLanguage shouldBe "kotlin"
            repositories shouldBe searchResult.items
            currentPage shouldBe 1
            hasNextPage shouldBe true
            error shouldBe null
            isLoading shouldBe false
        }
    }

    test("search should handle error state correctly").config(
        coroutineTestScope = true
    ) {
        coEvery {
            useCase.searchRepos(
                language = any(),
                query = any(),
                page = any()
            )
        } returns Result.failure(Exception(mockErrorMessage))

        viewModel.onQueryChange("test")
        testCoroutineScheduler.advanceUntilIdle()

        with(viewModel.uiState.value) {
            error shouldBe mockErrorMessage
            isLoading shouldBe false
            repositories shouldBe emptyList()
        }
    }
})