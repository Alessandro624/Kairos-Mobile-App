package it.unical.demacs.informatica.kairosapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.swagger.client.models.EventDTO

@Database(entities = [EventDTO::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var localInstance = instance
                if (localInstance == null) {
                    localInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "kairos-database"
                    ).build()
                    instance = localInstance
                }
                return localInstance
            }
        }
    }
}
