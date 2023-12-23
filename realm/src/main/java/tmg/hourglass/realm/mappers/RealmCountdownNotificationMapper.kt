package tmg.hourglass.realm.mappers

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.hourglass.domain.model.CountdownNotifications
import tmg.hourglass.realm.models.RealmCountdown
import tmg.hourglass.realm.models.RealmCountdownNotifications
import java.lang.RuntimeException
import javax.inject.Inject

class RealmCountdownNotificationMapper @Inject constructor() {

    private val DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm")

    fun deserialize(input: RealmCountdownNotifications): CountdownNotifications? {
        return if (input.atTime.isNotEmpty()) {
            try {
                CountdownNotifications.AtTime(_id = input.id, time = LocalDateTime.parse(input.atTime, DATE_TIME_FORMAT))
            } catch (e: RuntimeException) {
                null
            }
        } else {
            CountdownNotifications.AtValue(_id = input.id, value = input.atValue)
        }
    }

    fun serialize(model: RealmCountdownNotifications, data: CountdownNotifications) {
        model.atTime = (data as? CountdownNotifications.AtTime)?.time?.format(DATE_TIME_FORMAT) ?: ""
        model.atValue = (data as? CountdownNotifications.AtValue)?.value ?: ""
    }
}