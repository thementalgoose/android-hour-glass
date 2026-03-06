package tmg.hourglass.room.mappers

import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.model.TagOrdering
import tmg.hourglass.room.models.sortKey
import javax.inject.Inject

internal class TagMapper @Inject constructor() {

    fun serialize(model: Tag): tmg.hourglass.room.models.Tag {
        return tmg.hourglass.room.models.Tag(
            id = model.tagId,
            name = model.name,
            colour = model.colour,
            sort = model.sort.sortKey
        )
    }

    fun deserialize(model: tmg.hourglass.room.models.Tag): Tag {
        return Tag(
            tagId = model.id,
            name = model.name,
            colour = model.colour,
            sort = model.sort.toTagOrdering()
        )
    }

    private fun String.toTagOrdering(): TagOrdering {
        TagOrdering.entries.firstOrNull { it.sortKey == this } ?: TagOrdering.ALPHABETICAL
    }
}