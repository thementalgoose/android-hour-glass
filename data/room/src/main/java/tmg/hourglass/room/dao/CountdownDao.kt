package tmg.hourglass.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tmg.hourglass.room.models.Countdown
import java.time.LocalDateTime
import java.time.ZoneOffset

@Dao
internal interface CountdownDao {

    @Query("SELECT * FROM Countdown")
    fun getCountdowns(): Flow<List<Countdown>>

    @Query("SELECT * FROM Countdown WHERE id == :id")
    fun getCountdown(id: String): Flow<Countdown?>

    @Query("DELETE FROM Countdown WHERE id == :id")
    suspend fun deleteCountdown(id: String)

    @Query("DELETE FROM Countdown")
    suspend fun deleteAllCountdowns()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountdown(countdown: Countdown)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCountdown(countdown: List<Countdown>)
}