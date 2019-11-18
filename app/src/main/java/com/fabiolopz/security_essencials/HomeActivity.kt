package com.fabiolopz.security_essencials

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity() {
    //region statement
    private lateinit var home: TextView
    private lateinit var email: TextView
    private lateinit var latAndLong: TextView
    private lateinit var phone1: TextView
    private lateinit var phone2: TextView
    private lateinit var buttonGetPhone1: Button
    private lateinit var buttonGetPhone2: Button
    private lateinit var buttonSave: Button
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initComponent()
        showToolbar(resources.getString(R.string.txt_title_home), false)
    }

    private fun getCurrentUser(){
        val user:FirebaseUser? = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name: String? = user.displayName
            val email: String? = user.email
            val photoUrl: Uri? = user.photoUrl

            // Check if user's email is verified
            val emailVerified: Boolean = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid: String = user.uid
        }
    }

    private fun showToolbar(title:String, upButton:Boolean){
        val toolbar = findViewById<Toolbar>(R.id.toolbarH)
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
    }

    private fun initComponent(){
        home = findViewById(R.id.textViewNameH)
        email = findViewById(R.id.textViewEmailH)
        latAndLong = findViewById(R.id.textViewLatAndLongH)
        buttonGetPhone1 = findViewById(R.id.buttonGetPhone1H)
        phone1 = findViewById(R.id.textViewGet1H)
        buttonGetPhone2 = findViewById(R.id.buttonGetPhone2H)
        phone2 = findViewById(R.id.textViewGet2H)
        buttonSave = findViewById(R.id.buttonSaveH)
    }
}
