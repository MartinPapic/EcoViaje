package com.appecoviaje.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Trip::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Instance?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.tripDao().insert(Trip(title = "Senderismo en la Sierra", description = "Una aventura de senderismo para todos los niveles.", location = "Sierra Nevada"))
                                    database.tripDao().insert(Trip(title = "Tour de Observación de Aves", description = "Descubre la avifauna local en su hábitat natural.", location = "Parque Natural Doñana"))
                                    database.tripDao().insert(Trip(title = "Ruta Gastronómica Sostenible", description = "Saborea productos locales y ecológicos.", location = "La Alpujarra"))
                                    database.tripDao().insert(Trip(title = "Ciclismo por la Vía Verde", description = "Un recorrido en bicicleta por antiguas vías de tren.", location = "Vía Verde de la Sierra"))
                                }
                            }
                        }
                    })
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
