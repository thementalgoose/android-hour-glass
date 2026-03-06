package tmg.hourglass.room.mappers

import tmg.hourglass.domain.model.Tag
import javax.inject.Inject

internal class TagMapper @Inject constructor() {

    fun serialize(model: Tag): tmg.hourglass.room.models.Tag {
        return tmg.hourglass.room.models.Tag(
            id = model.tagId,
            name = model.name,
            colour = model.colour
        )
    }

    fun deserialize(model: tmg.hourglass.room.models.Tag): Tag {
        return Tag(
            tagId = model.id,
            name = model.name,
            colour = model.colour
        )
    }
}