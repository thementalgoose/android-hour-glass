package tmg.hourglass.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tmg.hourglass.room.models.Tag

@Dao
internal interface TagDao {

    @Query("SELECT * FROM Tag")
    fun getTags(): Flow<List<Tag>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    @Query("DELETE FROM Tag WHERE id == :id")
    suspend fun deleteTag(id: String)
}