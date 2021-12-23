package com.example.l01_v12_3_2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {
    @Insert
    fun insert(user: User)

    @Query("SELECT password=:password FROM User where email=:email")
    fun checkCredentials(email: String, password: String): Boolean

    @Query("SELECT NOT EXISTS(select 1 from User where email=:email)")
    fun isEmailUnique(email: String): Boolean
}