package com.fabiolopz.security_essencials

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class HomeActivity : AppCompatActivity() {
    //region statement
    private lateinit var home: TextView
    private lateinit var email: TextView
    private lateinit var latAndLong: TextView
    private lateinit var phone1: TextView
    private lateinit var phone2: TextView
    private lateinit var getPhone1: Button
    private lateinit var getPhone2: Button
    private lateinit var save: Button
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initComponent()
        showToolbar(resources.getString(R.string.txt_title_home), false)
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
        getPhone1 = findViewById(R.id.buttonGetPhone1H)
        phone1 = findViewById(R.id.textViewGet1H)
        getPhone2 = findViewById(R.id.buttonGetPhone2H)
        phone2 = findViewById(R.id.textViewGet2H)
        save = findViewById(R.id.buttonSaveH)
    }
}
