package tmg.hourglass.extensions

import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import java.text.ParseException
import kotlin.NullPointerException

fun String.toLocalDateTime(format: String = "dd/MM/yyyy HH:mm"): LocalDateTime {
    return try {
        LocalDateTime.parse(this, DateTimeFormatter.ofPattern(format))
    } catch (e: NullPointerException) {
        e.printStackTrace()
        LocalDateTime.MIN
    } catch (e: ParseException) {
        e.printStackTrace()
        LocalDateTime.MIN
    }
}

fun LocalDateTime.format(format: String = "dd/MM/yyyy HH:mm"): String {
    return this.format(DateTimeFormatter.ofPattern(format))
}

val LocalDateTime.millis: Long
    get() = this.toInstant(ZoneOffset.UTC).toEpochMilli()

fun LocalDate.format(format: String = "dd/MM/yyyy"): String {
    return this.format(DateTimeFormatter.ofPattern(format))
}

fun LocalTime.format(format: String = "HH:mm"): String {
    return this.format(DateTimeFormatter.ofPattern(format))
}