package tmg.hourglass.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import tmg.hourglass.domain.model.Countdown
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.domain.model.TaggedCountdowns
import tmg.hourglass.domain.repositories.CountdownRepository
import tmg.hourglass.domain.repositories.PreferencesManager
import tmg.hourglass.domain.repositories.TagRepository
import java.time.LocalDateTime
import javax.inject.Inject

class GetTaggedCountdownsUseCase @Inject constructor(
    private val countdownRepository: CountdownRepository,
    private val tagRepository: TagRepository,
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke(): Flow<List<TaggedCountdowns>> {
        return combine(
            flow = tagRepository.getAll(),
            flow2 = countdownRepository.all(),
        ) { tags, countdowns ->

            val now = LocalDateTime.now()

            val countdownSet = countdowns.toMutableSet()
            return@combine buildList {
                for (tag in tags) {
                    val taggedCountdowns = countdowns.filter { it.tag == tag.tagId }
                        .also { countdownSet.removeAll(it) }
                    add(TaggedCountdowns.Tagged(
                        tag = tag,
                        countdowns = taggedCountdowns
                            .toList()
                            .sortBy(now, tag.sort)
                    ))
                }

                if (countdownSet.isNotEmpty()) {
                    add(TaggedCountdowns.Untagged(
                        sort = preferencesManager.sortOrder,
                        countdowns = countdownSet
                            .toList()
                            .sortBy(now, preferencesManager.sortOrder)
                        ))
                }
            }
        }
    }
}

fun List<Countdown>.sortBy(now: LocalDateTime, tagOrdering: TagOrdering): List<Countdown> {
    return when (tagOrdering) {
        TagOrdering.ALPHABETICAL -> this.sortedBy { it.name.lowercase() }
        TagOrdering.FINISHING_SOONEST -> this.sortedBy { it.endDate }
        TagOrdering.FINISHING_LATEST -> this.sortedByDescending { it.endDate }
        TagOrdering.PROGRESS -> this.sortedByDescending { it.getProgress(now) }
    }
}
