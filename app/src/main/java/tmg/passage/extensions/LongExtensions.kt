package tmg.passage.extensions

import org.threeten.bp.LocalDateTime
import tmg.passage.utils.LocalDateTimeUtils

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTimeUtils.ofMillis(this)
}