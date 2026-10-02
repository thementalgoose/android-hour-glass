package tmg.hourglass.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import tmg.hourglass.room.models.Countdown
import tmg.hourglass.room.models.CountdownWrapper
import tmg.hourglass.room.models.Notification

@Dao
internal interface CountdownDao {

    @Transaction
    @Query("SELECT * FROM Countdown")
    fun getCountdowns(): Flow<List<CountdownWrapper>>

    @Transaction
    @Query("SELECT * FROM Countdown WHERE id == :id")
    fun getCountdown(id: String): Flow<CountdownWrapper?>

    @Query("DELETE FROM Countdown WHERE id == :id")
    suspend fun deleteCountdown(id: String)

    @Query("DELETE FROM Countdown")
    suspend fun deleteAllCountdowns()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountdown(countdown: Countdown)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCountdown(countdown: List<Countdown>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<Notification>)

    @Query("DELETE FROM Notification WHERE countdown_id == :countdownId")
    suspend fun deleteNotificationsForCountdown(countdownId: String)

    @Transaction
    suspend fun upsertCountdownWithNotifications(
        countdown: Countdown,
        notifications: List<Notification>
    ) {
        insertCountdown(countdown)
        deleteNotificationsForCountdown(countdown.id)
        if (notifications.isNotEmpty()) {
            insertNotifications(notifications)
        }
    }
}