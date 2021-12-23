package com.example.l01_v12_3_2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    fun enableAdditionalFields(really: Boolean){
        val visibility = if(really) View.VISIBLE else GONE
        name.visibility = visibility
        passwordRepeated.visibility = visibility
        agreementCheckBox.visibility = visibility
        continueButton.isEnabled = !really || agreementCheckBox.isChecked
        email.error = null
        passwordRepeated.error = null
    }

    val isRegistering get() = tabLayout.selectedTabPosition == 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        enableAdditionalFields(false)
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                enableAdditionalFields(isRegistering)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        agreementCheckBox.setOnCheckedChangeListener { _, isChecked -> continueButton.isEnabled = isChecked }

        val sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val userDAO = UserDB.getInstance(this).userDAO()
        continueButton.setOnClickListener {
            if(isRegistering){
                val emailStr = email.text.toString()
                if(!userDAO.isEmailUnique(emailStr)) {
                    email.error = "Пользователь с таким адресом уже содержится в базе"
                    return@setOnClickListener
                }
                if(passwordRepeated.text.toString() !== password.text.toString()) {
                    passwordRepeated.error = "Пароли не совпадают"
                    return@setOnClickListener
                }
                userDAO.insert(
                    User(
                        name.text.toString(),
                        emailStr,
                        password.text.toString()
                    )
                )
            } else {
                if(!userDAO.checkCredentials(
                    email.text.toString(),
                    password.text.toString()
                )) {
                    email.error = "Пароль или логин введены неверно"
                    return@setOnClickListener
                }
            }
            sharedPref.edit().putBoolean("logged in", true).apply()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}