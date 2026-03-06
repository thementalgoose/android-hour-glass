package tmg.hourglass.domain.repositories

import kotlinx.coroutines.flow.Flow
import tmg.hourglass.domain.model.Tag

interface TagRepository {
    fun getAll(): Flow<List<Tag>>
    fun insertTag(tag: Tag)
    fun deleteTag(id: String)
    fun deleteTag(tag: Tag) {
        deleteTag(tag.tagId)
    }
}