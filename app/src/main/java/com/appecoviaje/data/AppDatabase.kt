package com.appecoviaje.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Trip::class, Experience::class, Reservation::class, User::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun experienceDao(): ExperienceDao
    abstract fun reservationDao(): ReservationDao
    abstract fun userDao(): UserDao

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
                                    database.tripDao().insert(Trip(title = "Torres del Paine", description = "El circuito de trekking más famoso de Chile y la Patagonia.", location = "Parque Nacional Torres del Paine"))
                                    database.tripDao().insert(Trip(title = "San Pedro de Atacama", description = "Un oasis en medio del desierto más árido del mundo.", location = "San Pedro de Atacama"))
                                    database.tripDao().insert(Trip(title = "Isla de Pascua", description = "La isla más remota del planeta, famosa por sus moáis.", location = "Isla de Pascua"))
                                    database.tripDao().insert(Trip(title = "Carretera Austral", description = "Una de las rutas más escénicas del mundo para recorrer en auto.", location = "Patagonia Chilena"))
                                    database.tripDao().insert(Trip(title = "Valle del Elqui", description = "El mejor lugar de Chile para la observación de estrellas.", location = "Valle del Elqui"))
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
