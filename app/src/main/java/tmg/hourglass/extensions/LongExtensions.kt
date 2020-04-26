package tmg.hourglass.extensions

import org.threeten.bp.LocalDateTime
import tmg.hourglass.utils.LocalDateTimeUtils

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTimeUtils.ofMillis(this)
}