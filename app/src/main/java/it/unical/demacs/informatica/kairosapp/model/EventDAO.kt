package it.unical.demacs.informatica.kairosapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.swagger.client.models.EventDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {
    @Query("select * from events order by title")
    fun getAllEvents(): Flow<List<EventDTO>>

    @Query("select * from events where id=:id")
    fun getEventById(id: String): Flow<EventDTO?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(eventDTO: EventDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventDTO>)

    @Update
    suspend fun update(eventDTO: EventDTO)
}
