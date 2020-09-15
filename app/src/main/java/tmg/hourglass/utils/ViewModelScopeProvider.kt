package tmg.hourglass.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

fun ViewModel.getScope(provider: CoroutineScope?): CoroutineScope = when (provider) {
    null -> viewModelScope
    else -> provider
}