package com.example.l01_v12_3_2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val loggedIn = sharedPref.getBoolean("logged in", false)

        startActivity(Intent(this, if(loggedIn) MainActivity::class.java else LoginActivity::class.java))
    }
}