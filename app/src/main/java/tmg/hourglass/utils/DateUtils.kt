package tmg.hourglass.utils

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object DateUtils {
    fun daysBetween(start: LocalDateTime, end: LocalDateTime): Int {
        return ChronoUnit.DAYS.between(start.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay()).toInt()
    }
}