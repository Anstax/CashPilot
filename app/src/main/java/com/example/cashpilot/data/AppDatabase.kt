package com.example.cashpilot.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cashpilot.data.dao.UserDao
import com.example.cashpilot.data.local.CreditOfferDao
import com.example.cashpilot.data.local.CreditOfferEntity
import com.example.cashpilot.data.model.User

@Database(
    entities = [User::class, CreditOfferEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun creditOfferDao(): CreditOfferDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cashpilot.db"
                )
                    .fallbackToDestructiveMigration() // 💥 удаляет старую БД, создаёт новую при изменении схемы
                    .build().also { INSTANCE = it }

            }
        }
    }
}
