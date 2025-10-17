package tmg.hourglass.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tmg.hourglass.room.models.WidgetReference

@Dao
internal interface WidgetDao {

    @Query("SELECT * FROM WidgetReference WHERE app_widget_id == :appWidgetId")
    fun getWidgetRef(appWidgetId: Int): Flow<WidgetReference?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWidgetRef(ref: WidgetReference)
}