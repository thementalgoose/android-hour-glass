package tmg.hourglass.extensions

import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

val LocalDateTime.millis: Long
    get() = this.toInstant(ZoneOffset.UTC).toEpochMilli()