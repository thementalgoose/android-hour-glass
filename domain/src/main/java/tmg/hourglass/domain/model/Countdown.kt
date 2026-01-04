package tmg.hourglass.domain.model

import tmg.hourglass.domain.BuildConfig
import tmg.hourglass.domain.enums.CountdownInterpolator
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.utils.ProgressUtils
import tmg.utilities.extensions.extend
import tmg.utilities.utils.LocalDateUtils
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.Year
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.ceil

sealed interface Countdown {

    val id: String
    val name: String
    val description: String
    val colour: String

    val startDate: LocalDateTime
    val endDate: LocalDateTime

    val startValue: String
    val endValue: String
    val countdownType: CountdownType

    val interpolator: CountdownInterpolator
        get() = CountdownInterpolator.LINEAR

    val notifications: List<CountdownNotifications>
        get() = emptyList()

    val startAtStartOfDay: LocalDateTime
        get() = startDate.toLocalDate().atTime(0, 0, 0, 0)

    val endAtStartOfDay: LocalDateTime
        get() = endDate.toLocalDate().atTime(0, 0, 0)

    val isFinished: Boolean
        get() = endDate <= LocalDateTime.now()

    fun getLabel(progress: Float): String {
        val start: Int = startValue.toIntOrNull() ?: 0
        val end: Int = endValue.toIntOrNull() ?: 100

        return countdownType.converter(ceil((start + (progress * (end - start)))).toInt().toString())
    }

    fun getProgress(now: LocalDateTime = LocalDateTime.now()): Float {
        return ProgressUtils.getProgress(this, now)
    }

    data class Static(
        override val id: String,
        override val name: String,
        override val description: String,
        override val colour: String,
        private val start: String,
        private val end: String,
        override val startValue: String,
        override val endValue: String,
        override val countdownType: CountdownType
    ): Countdown {
        override val startDate: LocalDateTime by lazy {
            val startLong = start.toLongOrNull()
            if (startLong != null) {
                return@lazy startLong.toLocalDateTime()
            }

            val startDateTime = start.toLocalDateTime()?.atStartOfDay(ZoneId.systemDefault())?.toLocalDateTime()
            if (startDateTime != null) {
                return@lazy startDateTime
            }

            return@lazy LocalDateTime.now()
        }

        override val endDate: LocalDateTime by lazy {
            val endLong = end.toLongOrNull()
            if (endLong != null) {
                return@lazy endLong.toLocalDateTime()
            }

            val endDateTime = end.toLocalDateTime()?.atStartOfDay(ZoneId.systemDefault())?.toLocalDateTime()
            if (endDateTime != null) {
                return@lazy endDateTime
            }

            return@lazy LocalDateTime.now()
        }

        override val isFinished: Boolean
            get() = endDate <= LocalDateTime.now()

        private fun String.toLocalDateTime(): LocalDate? {
            return try {
                LocalDate.parse(this, YYYY_MM_DD_FORMAT)
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
                null
            }
        }

        private fun Long.toLocalDateTime(): LocalDateTime {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)
        }

        companion object
    }

    data class Recurring(
        override val id: String,
        override val name: String,
        override val description: String,
        override val colour: String,
        private val day: Int,
        private val month: Month
    ): Countdown {

        override val isFinished: Boolean
            get() = false

        override val countdownType: CountdownType
            get() = CountdownType.DAYS

        override val startDate: LocalDateTime by lazy {
            return@lazy LocalDateTime.now()
        }

        override val endDate: LocalDateTime by lazy {
            val string = "${Year.now().value}-${month.value.extend(2, '0')}-${day.extend(2, '0')}"
            val date = LocalDate.parse(string, YYYY_MM_DD_FORMAT)
            if (date < LocalDate.now()) {
                return@lazy date.plusYears(1L).atStartOfDay(ZoneId.systemDefault()).toLocalDateTime()
            }
            return@lazy date.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime()
        }

        override val startValue: String
            get() = LocalDateUtils.daysBetween(startDate.toLocalDate(), endDate.toLocalDate()).toString()

        override val endValue: String
            get() = "0"

        companion object
    }


    companion object {
        val YYYY_MM_DD_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.UK)
        val MM_DD_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd", Locale.UK)
    }
}

fun Countdown.Companion.preview(
    type: CountdownType = CountdownType.DAYS,
    color: String = "#152793",
): Countdown {
    return Countdown.Static(
        id = "countdown",
        name = "Countdown Item",
        description = "Generic Lorum Ipsum content here",
        colour = color,
        start = LocalDateTime.now().minusDays(1L).format(YYYY_MM_DD_FORMAT),
        end = LocalDateTime.now().plusDays(2L).format(YYYY_MM_DD_FORMAT),
        startValue = "0",
        endValue = "1000",
        countdownType = type,
    )
}