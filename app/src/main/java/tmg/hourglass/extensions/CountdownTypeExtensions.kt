package tmg.hourglass.extensions

import tmg.hourglass.R
import tmg.hourglass.data.CountdownType

fun CountdownType.label(): Int {
    return when (this) {
        CountdownType.NUMBER -> R.string.countdown_type_number
        CountdownType.DAYS -> R.string.countdown_type_days
        CountdownType.MONEY_GBP -> R.string.countdown_type_money_gbp
        CountdownType.MONEY_USD -> R.string.countdown_type_money_usd
        CountdownType.MONEY_EUR -> R.string.countdown_type_money_eur
        CountdownType.MONEY_YEN -> R.string.countdown_type_money_yen
        CountdownType.MILLIMETRES -> R.string.countdown_type_millimetres
        CountdownType.METRES -> R.string.countdown_type_metres
        CountdownType.KILOMETRES -> R.string.countdown_type_kilometres
        CountdownType.MILES -> R.string.countdown_type_miles
        CountdownType.GRAMS -> R.string.countdown_type_grams
        CountdownType.KILOGRAMS -> R.string.countdown_type_kilograms
        CountdownType.MILLILITRES -> R.string.countdown_type_millilitres
        CountdownType.LITRES -> R.string.countdown_type_litres
        CountdownType.SECONDS -> R.string.countdown_type_seconds
    }
}