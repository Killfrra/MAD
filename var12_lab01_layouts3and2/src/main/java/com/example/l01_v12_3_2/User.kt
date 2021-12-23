package com.example.l01_v12_3_2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User(
    val name: String,
    @PrimaryKey val email: String,
    val password: String
)
