package tmg.hourglass.testutils

import androidx.annotation.CallSuper
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

// https://stackoverflow.com/questions/62332403/how-to-inject-viewmodelscope-for-android-unit-test-with-kotlin-coroutines

@ExtendWith(TestingTaskExecutor::class)
internal open class BaseTest {

    @get:Rule
    val coroutineScope = CoroutineRule()

    private val testDispatcher = coroutineScope.testDispatcher
    private val testScope = coroutineScope.testScope

    @BeforeEach
    @CallSuper
    open fun beforeAll() {
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Run a test with test coroutine scope
     * - advanceUntilIdle()
     */
    fun coroutineTest(block: TestCoroutineScope.() -> Unit) {
        runBlockingTest(testDispatcher) {
            block(this)
        }
    }
}

/**
 * Task executor to set threads to using main
 */
private class TestingTaskExecutor: BeforeEachCallback, AfterEachCallback {
    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(TestTaskExecutor)
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}

object TestTaskExecutor: TaskExecutor() {
    override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
    override fun isMainThread(): Boolean = true
    override fun postToMainThread(runnable: Runnable) = runnable.run()
}