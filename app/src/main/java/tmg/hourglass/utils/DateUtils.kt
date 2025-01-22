package tmg.hourglass.utils

import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit

object DateUtils {
    fun daysBetween(start: LocalDateTime, end: LocalDateTime): Int {
        return ChronoUnit.DAYS.between(start.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay()).toInt()
    }
}