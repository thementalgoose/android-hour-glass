package tmg.hourglass.data

enum class CountdownType(
    val key: String,
    val converter: (value: String) -> String = { it }
) {
    NUMBER(
        key = "NUMBER"
    ),
    MILES(
        key = "MILES",
        converter = { "$it mi" }
    ),
    KILOMETRES(
        key = "KILOMETRES",
        converter = { "$it km" }
    ),
    MONEY_GBP(
        key = "MONEY_GBP",
        converter = { "£ $it" }
    ),
    MONEY_USD(
        key = "MONEY_USD",
        converter = { "$ $it" }
    ),
    MONEY_EUR(
        key = "MONEY_EUR",
        converter = { "€ $it" }
    )
//    COUNTDOWN(
//        key = "COUNTDOWN",
//        converter = { "$it days" }
//    ),
//    DAYS(
//        key = "DAYS",
//        converter = { "$it days" }
//    )
}