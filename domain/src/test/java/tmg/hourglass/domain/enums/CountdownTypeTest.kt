package tmg.hourglass.domain.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class CountdownTypeTest {

    @ParameterizedTest
    @CsvSource(
        "NUMBER,1,1",
        "DAYS,1,1 days",
        "MILES,1,1mi",
        "MILLIMETRES,1,1mm",
        "METRES,1,1m",
        "KILOMETRES,1,1km",
        "MONEY_GBP,1,£1",
        "MONEY_USD,1,\$1",
        "MONEY_EUR,1,€1",
        "MONEY_YEN,1,¥1",
        "KILOGRAMS,1,1kg",
        "GRAMS,1,1g",
        "LITRES,1,1l",
        "MILLILITRES,1,1ml",
        "SECONDS,1,1s",
        "TEMPERATURE_CELSIUS,1,1°C",
        "TEMPERATURE_FAHRENHEIT,1,1°F",
    )
    fun `converter prints expected result`(inputType: CountdownType, value: String, expectedOutput: String) {
        assertEquals(expectedOutput, inputType.converter(value))
    }
}