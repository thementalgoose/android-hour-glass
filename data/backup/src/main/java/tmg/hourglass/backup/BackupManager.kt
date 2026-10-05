package tmg.hourglass.backup

import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import tmg.hourglass.backup.models.v1.BackupSchemaV1
import tmg.hourglass.backup.models.v1.CountdownV1
import tmg.hourglass.backup.models.v1.NotificationV1
import tmg.hourglass.backup.models.v1.TagV1
import tmg.hourglass.domain.enums.CountdownType
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.CountdownNotifications
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.TagRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject

class BackupManager @Inject constructor(
    private val countdownRepository: CountdownRepository,
    private val tagRepository: TagRepository
) {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    suspend fun generateBackupJSON(): String {
        val tags = tagRepository.getAll().first()
        val countdowns = countdownRepository.all().first()

        val tagV1List = tags.map { tag ->
            TagV1(
                id = tag.tagId,
                name = tag.name,
                colour = tag.colour,
                sort = tag.sort.name,
                expanded = tag.expanded
            )
        }

        val countdownV1List = countdowns.map { countdown ->
            val (startStr, endStr) = when (countdown) {
                is Countdown.Recurring -> {
                    val s = countdown.startDate.format(Countdown.YYYY_MM_DD_FORMAT)
                    val e = countdown.endDate.format(Countdown.MM_DD_FORMAT)
                    s to e
                }
                is Countdown.Static -> {
                    val s = countdown.startDate.format(Countdown.YYYY_MM_DD_FORMAT)
                    val e = countdown.endDate.format(Countdown.YYYY_MM_DD_FORMAT)
                    s to e
                }
            }

            val notificationsV1 = countdown.notifications.map { notif ->
                when (notif) {
                    is CountdownNotifications.AtTime -> NotificationV1(
                        id = notif.id,
                        type = "TIME",
                        time = notif.time.format(Countdown.YYYY_MM_DD_FORMAT)
                    )
                    is CountdownNotifications.AtValue -> NotificationV1(
                        id = notif.id,
                        type = "VALUE",
                        value = notif.value
                    )
                }
            }

            CountdownV1(
                id = countdown.id,
                name = countdown.name,
                description = countdown.description,
                colour = countdown.colour,
                start = startStr,
                end = endStr,
                initial = countdown.startValue,
                finishing = countdown.endValue,
                passageType = countdown.countdownType.key,
                isRecurring = countdown is Countdown.Recurring,
                interpolator = countdown.interpolator.key,
                tagId = countdown.tag?.tagId,
                emoji = countdown.emoji,
                notifications = notificationsV1
            )
        }

        val schemaV1 = BackupSchemaV1(
            schemaVersion = "v1",
            tags = tagV1List,
            countdowns = countdownV1List
        )

        return json.encodeToString(schemaV1)
    }

    suspend fun restoreBackupJSON(jsonString: String): Boolean {
        return try {
            val jsonObject = json.parseToJsonElement(jsonString) as? JsonObject ?: return false
            val version = jsonObject["schemaVersion"]?.jsonPrimitive?.content ?: return false

            when (version) {
                "v1" -> {
                    val schemaV1 = json.decodeFromString<BackupSchemaV1>(jsonString)
                    restoreV1(schemaV1)
                    true
                }
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun restoreV1(schemaV1: BackupSchemaV1) {
        val restoredTags = schemaV1.tags.map { tagV1 ->
            Tag(
                tagId = tagV1.id,
                name = tagV1.name,
                colour = tagV1.colour,
                sort = try { TagOrdering.valueOf(tagV1.sort) } catch (_: Exception) { TagOrdering.ALPHABETICAL },
                expanded = tagV1.expanded
            )
        }
        val tagMap = restoredTags.associateBy { it.tagId }

        restoredTags.forEach { tagRepository.insertTag(it) }

        val restoredCountdowns = schemaV1.countdowns.map { cV1 ->
            val tag = cV1.tagId?.let { tagMap[it] }
            val notifications = cV1.notifications.map { nV1 ->
                when (nV1.type) {
                    "TIME" -> CountdownNotifications.AtTime(
                        id = nV1.id,
                        time = parseTime(nV1.time)
                    )
                    else -> CountdownNotifications.AtValue(
                        id = nV1.id,
                        value = nV1.value.orEmpty()
                    )
                }
            }

            if (cV1.isRecurring) {
                val parts = cV1.end.split("-")
                val monthVal = parts.getOrNull(0)?.toIntOrNull() ?: 12
                val dayVal = parts.getOrNull(1)?.toIntOrNull() ?: 31
                Countdown.Recurring(
                    id = cV1.id,
                    name = cV1.name,
                    description = cV1.description,
                    colour = cV1.colour,
                    emoji = cV1.emoji,
                    day = dayVal,
                    month = Month.of(monthVal),
                    tag = tag,
                    notifications = notifications
                )
            } else {
                val passageType = CountdownType.entries.firstOrNull { it.key == cV1.passageType } ?: CountdownType.NUMBER
                Countdown.Static(
                    id = cV1.id,
                    name = cV1.name,
                    description = cV1.description,
                    colour = cV1.colour,
                    emoji = cV1.emoji,
                    start = cV1.start,
                    end = cV1.end,
                    startValue = cV1.initial,
                    endValue = cV1.finishing,
                    countdownType = passageType,
                    tag = tag,
                    notifications = notifications
                )
            }
        }

        countdownRepository.saveAll(restoredCountdowns)
    }

    private fun parseTime(timeStr: String?): LocalDateTime {
        if (timeStr == null) return LocalDateTime.now()
        return try {
            LocalDate.parse(timeStr, Countdown.YYYY_MM_DD_FORMAT).atStartOfDay()
        } catch (_: Exception) {
            try {
                LocalDateTime.parse(timeStr)
            } catch (_: Exception) {
                LocalDateTime.now()
            }
        }
    }
}
