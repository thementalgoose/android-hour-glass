package tmg.hourglass.extensions

import tmg.hourglass.strings.R.string
import tmg.hourglass.domain.enums.CountdownType

fun CountdownType.label(): Int {
    return when (this) {
        CountdownType.NUMBER -> string.countdown_type_number
        CountdownType.DAYS -> string.countdown_type_days
        CountdownType.MONEY_GBP -> string.countdown_type_money_gbp
        CountdownType.MONEY_USD -> string.countdown_type_money_usd
        CountdownType.MONEY_EUR -> string.countdown_type_money_eur
        CountdownType.MONEY_YEN -> string.countdown_type_money_yen
        CountdownType.MILLIMETRES -> string.countdown_type_millimetres
        CountdownType.METRES -> string.countdown_type_metres
        CountdownType.KILOMETRES -> string.countdown_type_kilometres
        CountdownType.MILES -> string.countdown_type_miles
        CountdownType.GRAMS -> string.countdown_type_grams
        CountdownType.KILOGRAMS -> string.countdown_type_kilograms
        CountdownType.MILLILITRES -> string.countdown_type_millilitres
        CountdownType.LITRES -> string.countdown_type_litres
        CountdownType.SECONDS -> string.countdown_type_seconds
        CountdownType.TEMPERATURE_CELSIUS -> string.countdown_type_celsius
        CountdownType.TEMPERATURE_FAHRENHEIT -> string.countdown_type_fahrenheit
    }
}