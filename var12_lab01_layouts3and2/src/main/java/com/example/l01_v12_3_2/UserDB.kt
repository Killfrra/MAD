package com.example.l01_v12_3_2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UserDB : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    companion object {
        @Volatile
        private var INSTANCE: UserDB? = null
        fun getInstance(context: Context): UserDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDB::class.java,
                    "user_db"
                )
                    .allowMainThreadQueries() //TODO: dont allow
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}