package com.fabiolopz.security_essencials

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar

class LoginActivity : AppCompatActivity() {
    //region statement
    private lateinit var user: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var createAccount: Button
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initComponent()
        login.setOnClickListener {
            showHomeActivity()
        }
        createAccount.setOnClickListener {
            showCreateAccountActivity()
        }
    }

    private fun showHomeActivity(){
        val home = Intent(this, HomeActivity::class.java)
        startActivity(home)
    }

    private fun showCreateAccountActivity(){
        val createAccount = Intent(this, CreateAccountActivity::class.java)
        startActivity(createAccount)
    }

    private fun initComponent(){
        user = findViewById(R.id.editTextUserL)
        password = findViewById(R.id.editTextPasswordL)
        login = findViewById(R.id.buttonLoginL)
        createAccount = findViewById(R.id.buttonCreateAccountL)
    }
}
