package com.android.mygithub

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MyTest() : FunSpec() {
    private val scope = TestScope()
    private val testDispatcher: CoroutineDispatcher = StandardTestDispatcher(scope.testScheduler)

    override fun beforeTest(f: suspend (TestCase) -> Unit) {
        super.beforeTest(f)
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterSpec(f: suspend (Spec) -> Unit) {
        super.afterSpec(f)
        Dispatchers.resetMain()
    }

}