package com.fabiolopz.security_essentials

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    //region statement
    private lateinit var user: User
    private lateinit var fieldEmail: EditText
    private lateinit var fieldPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonCreateAccount: Button
    //FireBase
    private lateinit var auth: FirebaseAuth
    private val keySecret: String = "fabio andres lopez perez"
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Call Function
        initComponent()
        buttonLogin.setOnClickListener(this)
        buttonCreateAccount.setOnClickListener(this)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser:FirebaseUser? = auth.currentUser
        if(currentUser != null){
            user = User(currentUser.uid, currentUser.displayName, currentUser.email!!)
            Log.d(TAG, "usuario: ${user.email} success")
            showHomeActivity()
        }
    }

    private fun singIn(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val currentUser: FirebaseUser? = auth.currentUser
                    if(currentUser != null) {
                        var name:String? = currentUser.displayName
                        if(name == "" || name == null){
                            name = "N/A"
                        }
                        user =
                            User(currentUser.uid, name, currentUser.email!!)
                        saveUserSharedPreferences(user)
                        showHomeActivity()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true
        val email:String = fieldEmail.text.toString()
        val password:String = fieldPassword.text.toString()
        if (email.isEmpty()) {
            utilInputEmailL.error = "Required."
            valid = false
        } else {
            utilInputEmailL.error = null
        }
        if (password.isEmpty()) {
            utilInputPasswordL.error = "Required."
            valid = false
        } else {
            utilInputPasswordL.error = null
        }
        return valid
    }

    private fun saveUserSharedPreferences(user: User){
        val preference: SharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val  editor: SharedPreferences.Editor = preference.edit()
        editor.putString("uid", user.UID)
        editor.putString("email", user.email)
        editor.putString("name", "N/A")
        editor.commit()
    }

    private fun showHomeActivity(){
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun showCreateAccountActivity(){
        val createAccount = Intent(this, CreateAccountActivity::class.java)
        startActivity(createAccount)
    }

    private fun initComponent(){
        fieldEmail = findViewById(R.id.editTextEmailL)
        fieldPassword = findViewById(R.id.editTextPasswordL)
        buttonLogin= findViewById(R.id.buttonLoginL)
        buttonCreateAccount = findViewById(R.id.buttonCreateAccountL)
        auth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.buttonLoginL -> {
                if(validateForm()){
                    singIn(fieldEmail.text.toString(), String.encrypt(fieldPassword.text.toString()))
                }
            }
            R.id.buttonCreateAccountL -> showCreateAccountActivity()
        }
    }

    companion object {
        const val TAG = "Message_L_Activity"
    }

    private fun String.Companion.encrypt(password: String): String {
        val secretKeySpec = SecretKeySpec(keySecret.toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = password.toCharArray()
        for (i in charArray.indices) {
            iv[i] = charArray[i].toByte()
        }
        val ivParameterSPec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSPec)
        val encryptedValue = cipher.doFinal()
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    }
}
