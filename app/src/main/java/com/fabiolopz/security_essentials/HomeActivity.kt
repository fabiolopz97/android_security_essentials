package com.fabiolopz.security_essentials

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class HomeActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener{
    //region statement
    private lateinit var user:User
    private lateinit var fieldName: TextView
    private lateinit var fieldEmail: TextView
    private lateinit var fieldLatAndLong: TextView
    private lateinit var fieldPhone1: TextView
    private lateinit var fieldPhone2: TextView
    private lateinit var buttonGetPhone1: Button
    private lateinit var buttonGetPhone2: Button
    private lateinit var buttonSave: Button
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initComponent()
        showToolbar(resources.getString(R.string.txt_title_home), false)

        val dataIntent:Bundle = intent.extras
        val user:User = dataIntent.getSerializable(OBJ_USER) as User
        updateDataUI(user)
        //Log.d(TAG, "user : ${user.name}")

        /*val apiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addConnectionCallbacks(this)
            .addApi(LocationServices.API)
            .build()*/
    }



    private fun getCurrentUser(){
        val currentUser:FirebaseUser? = FirebaseAuth.getInstance().currentUser
        user = User(currentUser!!.uid, currentUser.displayName!!, currentUser.email!!)
        /*user?.let {
            // Name, email address, and profile photo Url
            val name: String? = user.displayName
            val email: String? = user.email
            //val photoUrl: Uri? = user.photoUrl

            // Check if user's email is verified
            //val emailVerified: Boolean = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid: String = user.uid
        }*/
    }

    private fun updateDataUI(user: User){
        if(user.name != ""){
            fieldName.text = user.name
        } else {
            fieldName.text = "N/A"
        }

        fieldEmail.text = user.email
    }

    private fun showToolbar(title:String, upButton:Boolean){
        val toolbar: Toolbar = findViewById(R.id.toolbarH)
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
    }

    private fun initComponent(){
        fieldName = findViewById(R.id.textViewNameH)
        fieldEmail = findViewById(R.id.textViewEmailH)
        fieldLatAndLong = findViewById(R.id.textViewLatAndLongH)
        buttonGetPhone1 = findViewById(R.id.buttonGetPhone1H)
        fieldPhone1 = findViewById(R.id.textViewGet1H)
        buttonGetPhone2 = findViewById(R.id.buttonGetPhone2H)
        fieldPhone2 = findViewById(R.id.textViewGet2H)
        buttonSave = findViewById(R.id.buttonSaveH)
    }

    companion object {
        const val TAG = "Message_H_Activity"
        const val LOG_TAG = "Message_Log"
        const val OBJ_USER = "User"
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.
        Log.e(LOG_TAG, "Error grave al conectar con Google Play Services");
    }
}
