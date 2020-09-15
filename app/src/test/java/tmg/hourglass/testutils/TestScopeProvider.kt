package tmg.hourglass.testutils

import kotlinx.coroutines.CoroutineScope
import tmg.hourglass.di.async.ScopeProvider

class TestScopeProvider(
    private val scope: CoroutineScope
): ScopeProvider {
    override fun getCoroutineScope(): CoroutineScope? = scope
}