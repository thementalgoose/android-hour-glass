package tmg.hourglass.domain.model

import java.time.LocalDateTime

sealed class CountdownNotifications(
    open val id: String
) {
    data class AtTime(
        override val id: String,
        val time: LocalDateTime
    ): CountdownNotifications(id = id) {
        companion object
    }

    data class AtValue(
        override val id: String,
        val value: String
    ): CountdownNotifications(id = id) {
        companion object
    }

    companion object
}

typealias CountdownNotification = CountdownNotifications