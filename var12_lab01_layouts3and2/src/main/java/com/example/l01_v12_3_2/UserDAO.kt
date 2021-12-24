package com.example.l01_v12_3_2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {
    @Insert
    fun insert(user: User)

    @Query("SELECT name FROM User WHERE email=:email AND password=:password")
    fun checkCredentials(email: String, password: String): String?

    @Query("SELECT NOT EXISTS(SELECT 1 FROM User WHERE email=:email)")
    fun isEmailUnique(email: String): Boolean
}