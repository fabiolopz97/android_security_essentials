package com.fabiolopz.security_essentials

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class HomeActivity : AppCompatActivity(), View.OnClickListener,
    GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
    LocationListener {

    //region statement
    private lateinit var user: User
    private lateinit var fieldName: TextView
    private lateinit var fieldEmail: TextView
    private lateinit var fieldLatAndLong: TextView
    private lateinit var fieldPhone1: TextView
    private lateinit var fieldPhone2: TextView
    private lateinit var buttonGetPhone1: Button
    private lateinit var buttonGetPhone2: Button
    private lateinit var buttonSave: Button
    private lateinit var buttonSignOutH: Button
    //FireBase
    private lateinit var auth: FirebaseAuth
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initComponent()
        showToolbar(resources.getString(R.string.txt_title_home), false)
        buttonGetPhone1.setOnClickListener(this)
        buttonGetPhone2.setOnClickListener(this)
        buttonSave.setOnClickListener(this)
        buttonSignOutH.setOnClickListener(this)

        showUserSharedPreferences()
        updateDataUI(user)
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addConnectionCallbacks(this)
            .addApi(LocationServices.API)
            .build()
    }

    private fun signOut() {
        auth.signOut()
        showLoginActivity()
    }

    private fun showLoginActivity() {
        val login = Intent(this, LoginActivity::class.java)
        startActivity(login)
    }

    private fun showUserSharedPreferences(){
        val preference: SharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val uid = preference.getString("uid", "")
        val email = preference.getString("email", "Error")
        val name = preference.getString("name", "N/A")
        user = User(uid, name, email)
    }

    private fun updateDataUI(user: User) {
        if (user.name != "") {
            fieldName.text = user.name
        } else {
            fieldName.text = "N/A"
        }
        fieldEmail.text = user.email
    }

    private fun updateLocation(){
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = UPDATE_INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
    }

    private fun updateUI(loc: Location?) {
        if (loc != null) {
            fieldLatAndLong.text = "Latitude: ${loc.latitude}\n" +
                    "Longitude: ${loc.longitude}\n"
        } else {
            fieldLatAndLong.text = "Latitude: (unknown)\nLongitude: (unknown)"
        }
    }

    private fun showToolbar(title: String, upButton: Boolean) {
        val toolbar: Toolbar = findViewById(R.id.toolbarH)
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
    }

    private fun initComponent() {
        fieldName = findViewById(R.id.textViewNameH)
        fieldEmail = findViewById(R.id.textViewEmailH)
        fieldLatAndLong = findViewById(R.id.textViewLatAndLongH)
        buttonGetPhone1 = findViewById(R.id.buttonGetPhone1H)
        fieldPhone1 = findViewById(R.id.textViewGet1H)
        buttonGetPhone2 = findViewById(R.id.buttonGetPhone2H)
        fieldPhone2 = findViewById(R.id.textViewGet2H)
        buttonSave = findViewById(R.id.buttonSaveH)
        buttonSignOutH = findViewById(R.id.buttonSignOutH)
        auth = FirebaseAuth.getInstance()
        locationRequest = LocationRequest()
    }

    companion object {
        const val TAG = "Message_H_Activity"
        const val LOG_TAG = "Message_Log"
        //region statement
        const val PETITION_PERMIT_LOCATION = 101
        const val UPDATE_INTERVAL: Long = 500
        const val FASTEST_INTERVAL: Long = 500
    }

    // Events
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonSignOutH -> signOut()
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.
        Log.e(LOG_TAG, "Error grave al conectar con Google Play Services")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PETITION_PERMIT_LOCATION) {
            if (grantResults.size == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) { //Permiso concedido
                val location =
                    LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                updateUI(location)
            } else { //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.
                Log.e(LOG_TAG, "Permiso denegado")
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        //Conectado correctamente a Google Play Services
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PETITION_PERMIT_LOCATION
            )
        } else {
            updateLocation()
            val location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
            updateUI(location)
        }
        //startLocationUpdate()
    }

    override fun onConnectionSuspended(p0: Int) {
        //Se ha interrumpido la conexión con Google Play Services
        Log.e(LOG_TAG, "Se ha interrumpido la conexión con Google Play Services")
    }

    override fun onLocationChanged(location: Location?) {
        updateLocation()
        updateUI(location)
    }

}
