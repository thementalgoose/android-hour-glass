package tmg.hourglass.widgets.presentation

import dagger.hilt.android.AndroidEntryPoint
import tmg.hourglass.widgets.presentation.single.CountdownWidgetReceiver as SingleCountdownWidgetReceiver

/**
 * Legacy receiver subclass kept for backwards compatibility with existing app widget installations.
 */
@AndroidEntryPoint
class CountdownWidgetReceiver : SingleCountdownWidgetReceiver()
