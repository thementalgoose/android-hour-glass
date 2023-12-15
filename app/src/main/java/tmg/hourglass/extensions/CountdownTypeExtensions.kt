package tmg.hourglass.extensions

import tmg.hourglass.realm.R.string as realmStrings
import tmg.hourglass.domain.enums.CountdownType

fun CountdownType.label(): Int {
    return when (this) {
        CountdownType.NUMBER -> realmStrings.countdown_type_number
        CountdownType.DAYS -> realmStrings.countdown_type_days
        CountdownType.MONEY_GBP -> realmStrings.countdown_type_money_gbp
        CountdownType.MONEY_USD -> realmStrings.countdown_type_money_usd
        CountdownType.MONEY_EUR -> realmStrings.countdown_type_money_eur
        CountdownType.MONEY_YEN -> realmStrings.countdown_type_money_yen
        CountdownType.MILLIMETRES -> realmStrings.countdown_type_millimetres
        CountdownType.METRES -> realmStrings.countdown_type_metres
        CountdownType.KILOMETRES -> realmStrings.countdown_type_kilometres
        CountdownType.MILES -> realmStrings.countdown_type_miles
        CountdownType.GRAMS -> realmStrings.countdown_type_grams
        CountdownType.KILOGRAMS -> realmStrings.countdown_type_kilograms
        CountdownType.MILLILITRES -> realmStrings.countdown_type_millilitres
        CountdownType.LITRES -> realmStrings.countdown_type_litres
        CountdownType.SECONDS -> realmStrings.countdown_type_seconds
        CountdownType.TEMPERATURE_CELSIUS -> realmStrings.countdown_type_celsius
        CountdownType.TEMPERATURE_FAHRENHEIT -> realmStrings.countdown_type_fahrenheit
    }
}