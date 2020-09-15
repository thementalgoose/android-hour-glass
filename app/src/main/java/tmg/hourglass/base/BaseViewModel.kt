package tmg.hourglass.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import tmg.hourglass.di.async.ScopeProvider
import tmg.hourglass.utils.getScope

abstract class BaseViewModel(
    scopeProvider: ScopeProvider
): ViewModel() {

    val scope: CoroutineScope = getScope(scopeProvider.getCoroutineScope())
}