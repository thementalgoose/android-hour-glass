package tmg.hourglass.domain.model

import java.time.LocalDateTime

sealed class CountdownNotifications(
    val id: String
) {
    data class AtTime(
        private val _id: String,
        val time: LocalDateTime
    ): CountdownNotifications(id = _id) {
        companion object
    }

    data class AtValue(
        private val _id: String,
        val value: String
    ): CountdownNotifications(id = _id) {
        companion object
    }

    companion object
}