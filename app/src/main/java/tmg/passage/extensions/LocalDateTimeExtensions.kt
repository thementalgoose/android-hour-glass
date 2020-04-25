package tmg.passage.extensions

import org.threeten.bp.LocalDateTime
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