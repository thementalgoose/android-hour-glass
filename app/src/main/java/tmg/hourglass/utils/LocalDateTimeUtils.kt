package tmg.hourglass.utils

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

class LocalDateTimeUtils {

    companion object {
        @JvmStatic
        fun ofMillis(millis: Long): LocalDateTime {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC)
        }
    }
}