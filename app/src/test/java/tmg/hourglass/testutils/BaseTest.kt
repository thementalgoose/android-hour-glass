package tmg.hourglass.testutils

import androidx.annotation.CallSuper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import tmg.hourglass.di.async.ScopeProvider

// https://stackoverflow.com/questions/62332403/how-to-inject-viewmodelscope-for-android-unit-test-with-kotlin-coroutines

@ExperimentalCoroutinesApi
@ExtendWith(TestingTaskExecutor::class)
open class BaseTest {

    @get:Rule
    val coroutineScope = CoroutineRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    val testScopeProvider = TestScopeProvider(testScope)

    @BeforeEach
    @CallSuper
    private fun beforeAll() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    @CallSuper
    private fun afterAll() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        testScope.cleanupTestCoroutines()
    }

    fun coroutineTest(block: TestCoroutineScope.() -> Unit) {
        runBlockingTest(testDispatcher) {
            block(this)
        }
    }
}