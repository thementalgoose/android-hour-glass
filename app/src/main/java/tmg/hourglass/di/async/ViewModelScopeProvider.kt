package tmg.hourglass.di.async

import kotlinx.coroutines.CoroutineScope

class ViewModelScopeProvider: ScopeProvider {
    override fun getCoroutineScope(): CoroutineScope? = null
}