package tmg.hourglass.migration

import kotlinx.coroutines.flow.firstOrNull
import tmg.hourglass.core.crashlytics.AnalyticsManager
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.TagRepository
import javax.inject.Inject

/**
 * Migration script for logging existing events for
 *  existing users as a patch so we can see how many
 *  has been added.
 *
 * Added: March 9th
 * Remove this in a few months
 */
class LogOldEvents @Inject constructor(
    private val analyticsManager: AnalyticsManager,
    private val countdownRepository: CountdownRepository,
    private val tagRepository: TagRepository
) {
    suspend fun invoke() {
        val countdowns = countdownRepository.all().firstOrNull() ?: emptyList()
        val tags = tagRepository.getAll().firstOrNull() ?: emptyList()

        countdowns.forEach { countdown ->
            val label = countdown.countdownType.key
            analyticsManager.event("countdown_add", mapOf(
                "type" to label
            ))
        }
        tags.forEach { _ ->
            analyticsManager.event("tag_add")
        }
    }
}