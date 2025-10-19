package tmg.hourglass.widgets.presentation

import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.WidgetReference

data class ConfigureUiState(
    val widgetReference: WidgetReference,
    val countdownModel: Countdown,
)