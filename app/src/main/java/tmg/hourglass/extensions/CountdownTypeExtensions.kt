package tmg.hourglass.extensions

import tmg.hourglass.R
import tmg.hourglass.data.CountdownType

fun CountdownType.label(): Int {
    return when (this) {
        CountdownType.NUMBER -> R.string.countdown_type_number
        CountdownType.MONEY_GBP -> R.string.countdown_type_money_gbp
        CountdownType.MONEY_USD -> R.string.countdown_type_money_usd
        CountdownType.MONEY_EUR -> R.string.countdown_type_money_eur
    }
}