package com.fabiolopz.security_essencials

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar

class CreateAccountActivity : AppCompatActivity() {
    //region statement
    private lateinit var user: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var login: Button
    private lateinit var createAccount: Button
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        showToolbar(resources.getString(R.string.toolbar_title_create_account))
        initComponent()
        login.setOnClickListener {
            showLoginActivity()
        }
    }

    private fun showLoginActivity(){
        val login = Intent(this, LoginActivity::class.java)
        startActivity(login)
    }

    private fun showToolbar(title:String, upButton:Boolean = true){
        val toolbar = findViewById<Toolbar>(R.id.toolbarCA)
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
    }

    private fun initComponent(){
        user = findViewById(R.id.editTextUserCA)
        password = findViewById(R.id.editTextPasswordCA)
        confirmPassword = findViewById(R.id.editTextConfirmPasswordCA)
        login = findViewById(R.id.buttonLoginCA)
        createAccount = findViewById(R.id.buttonCreateAccountCA)
    }
}
