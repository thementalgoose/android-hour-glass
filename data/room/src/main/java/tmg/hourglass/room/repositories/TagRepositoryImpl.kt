package tmg.hourglass.room.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import tmg.hourglass.domain.model.Tag
import tmg.hourglass.domain.repositories.TagRepository
import tmg.hourglass.room.dao.TagDao
import tmg.hourglass.room.mappers.TagMapper
import javax.inject.Inject

internal class TagRepositoryImpl @Inject constructor(
    private val tagDao: TagDao,
    private val tagMapper: TagMapper
): TagRepository {
    override fun getAll(): Flow<List<Tag>> {
        return tagDao.getTags()
            .map { list ->
                list.map { tagMapper.deserialize(it) }
            }
    }

    override fun insertTag(tag: Tag) {
        runBlocking {
            tagDao.insertTag(tagMapper.serialize(tag))
        }
    }

    override fun deleteTag(id: String) {
        runBlocking {
            tagDao.deleteTag(id)
        }
    }
}