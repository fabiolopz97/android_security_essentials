package com.fabiolopz.security_essencials

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var user: EditText
    private lateinit var password: EditText
    private lateinit var login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponent()

    }





    private fun initComponent(){
        user = findViewById(R.id.editTextPassword)
        password = findViewById(R.id.editTextUser)
        login = findViewById(R.id.buttonLogin)

    }
}
