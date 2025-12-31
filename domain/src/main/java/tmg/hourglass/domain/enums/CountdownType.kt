package tmg.hourglass.domain.enums

enum class CountdownType(
    val key: String,
    val converter: (value: String) -> String = { it }
) {
    DAYS(
        key = "DAYS",
        converter = {
            when (it) {
                "1" -> "$it day"
                else -> "$it days"
            }
        }
    ),
    NUMBER(
        key = "NUMBER"
    ),
    MILES(
        key = "MILES",
        converter = { "${it}mi" }
    ),
    MILLIMETRES(
        key = "MILLIMETRES",
        converter = { "${it}mm" }
    ),
    METRES(
        key = "METRES",
        converter = { "${it}m" }
    ),
    KILOMETRES(
        key = "KILOMETRES",
        converter = { "${it}km" }
    ),
    MONEY_GBP(
        key = "MONEY_GBP",
        converter = { "£$it" }
    ),
    MONEY_USD(
        key = "MONEY_USD",
        converter = { "$$it" }
    ),
    MONEY_EUR(
        key = "MONEY_EUR",
        converter = { "€$it" }
    ),
    MONEY_YEN(
        key = "MONEY_YEN",
        converter = { "¥$it" }
    ),
    KILOGRAMS(
        key = "KILOGRAMS",
        converter = { "${it}kg" }
    ),
    GRAMS(
        key = "GRAMS",
        converter = { "${it}g" }
    ),
    LITRES(
        key = "LITRES",
        converter = { "${it}l" }
    ),
    MILLILITRES(
        key = "MILLILITRES",
        converter = { "${it}ml"}
    ),
    SECONDS(
        key = "SECONDS",
        converter = { "${it}s"}
    ),
    TEMPERATURE_CELSIUS(
        key = "CELSIUS",
        converter = { "${it}°C"}
    ),
    TEMPERATURE_FAHRENHEIT(
        key = "FAHRENHEIT",
        converter = { "${it}°F"}
    );
}