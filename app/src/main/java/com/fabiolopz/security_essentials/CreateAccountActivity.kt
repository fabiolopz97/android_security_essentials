package com.fabiolopz.security_essentials

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class CreateAccountActivity : AppCompatActivity(), View.OnClickListener {

    //region statement
    private lateinit var user: User
    private lateinit var fieldEmail: EditText
    private lateinit var fieldPassword: EditText
    private lateinit var fieldConfirmPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonCreateAccount: Button
    // Write a message to the database
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    //Encrypt
    private val keySecret: String = "fabio andres lopez perez"
    private lateinit var secret: SecretKeySpec

    //FireBase
    private lateinit var auth: FirebaseAuth
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        showToolbar(resources.getString(R.string.toolbar_title_create_account))
        initComponent()
        buttonLogin.setOnClickListener(this)
        buttonCreateAccount.setOnClickListener(this)
        // Database
        myRef = database.reference
        //myRef.setValue("Hello, World!")
    }

    private fun validateForm(): Boolean {
        var valid = true
        val email: String = fieldEmail.text.toString()
        val password: String = fieldPassword.text.toString()
        val confirmPassword: String = fieldConfirmPassword.text.toString()

        if (email.isEmpty()) {
            utilInputEmailCA.error = "Required."
            valid = false
        } else {
            utilInputEmailCA.error = null
        }
        if (password.isEmpty()) {
            utilInputPasswordCA.error = "Required."
            valid = false
        } else {
            if (password.length < 6) {
                utilInputPasswordCA.error = "Password should be min 6."
                valid = false
            } else {
                utilInputPasswordCA.error = null
            }
        }
        if (confirmPassword.isEmpty()) {
            utilInputConfirmPassCA.error = "Required."
            valid = false
        } else {
            utilInputConfirmPassCA.error = null
        }
        if (validatePassword(password, confirmPassword)) {
            Toast.makeText(
                baseContext, "Incorrect password.",
                Toast.LENGTH_SHORT
            ).show()
            valid = false
        }
        return valid
    }

    private fun validatePassword(password: String, confirmPassword: String): Boolean =
        (password == "") || (confirmPassword == "")

    private fun writeNewUser(user: User) {
        //val user = User(userId, email)
        myRef.child("user").child(user.UID!!).setValue(user)
    }

    private fun saveUserSharedPreferences(user: User){
        val preference: SharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val  editor: SharedPreferences.Editor = preference.edit()
        editor.putString("uid", user.UID)
        editor.putString("email", user.email)
        editor.putString("name", "N/A")
        editor.commit()
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val currentUser: FirebaseUser? = auth.currentUser
                    user = User(currentUser!!.uid, currentUser.displayName, currentUser.email)
                    //Log.d(TAG, "usuario: ${user.email} success")
                    writeNewUser(user)
                    saveUserSharedPreferences(user)
                    showHomeActivity(user)
                    // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // updateUI(null)
                }
            }
    }

    /**
     * No son usados estos tres metodos en este proyecto
     */
    //Encrypt
    private fun generateKey(): SecretKey? {
        return SecretKeySpec(keySecret.toByteArray(), "AES").also { secret = it }
    }

    private fun encryptMsg(message: String, secret: SecretKey?): ByteArray? {
        var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        return cipher.doFinal(message.toByteArray(charset("UTF-8")))
    }

    private fun decryptMsg(cipherText: ByteArray?, secret: SecretKey?): String? {
        var cipher: Cipher? = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher?.init(Cipher.DECRYPT_MODE, secret)
        return String(cipher!!.doFinal(cipherText), charset("UTF-8"))
    }

    private fun showHomeActivity(user: User){
        val home = Intent(this, HomeActivity::class.java)
        user.name = ""
        home.putExtra(OBJ_USER, user)
        startActivity(home)
    }

    private fun showLoginActivity(){
        val login = Intent(this, LoginActivity::class.java)
        startActivity(login)
    }

    private fun showToolbar(title:String, upButton:Boolean = true){
        val toolbar:Toolbar = findViewById(R.id.toolbarCA)
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
    }

    private fun initComponent(){
        fieldEmail = findViewById(R.id.editTextEmailCA)
        fieldPassword = findViewById(R.id.editTextPasswordCA)
        fieldConfirmPassword = findViewById(R.id.editTextConfirmPasswordCA)
        buttonLogin = findViewById(R.id.buttonLoginCA)
        buttonCreateAccount = findViewById(R.id.buttonCreateAccountCA)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.buttonLoginCA -> showLoginActivity()
            R.id.buttonCreateAccountCA -> {
                if(validateForm()){
                    createAccount(fieldEmail.text.toString(), String.encrypt(fieldPassword.text.toString()))
                }
            }
        }
    }

    companion object {
        const val TAG = "Message_CA_Activity"
        const val OBJ_USER = "User"
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

    private fun String.Companion.decrypt(password: String): String{
        val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = password.toCharArray()
        for(element in charArray){
            iv[0] = element.toByte()
        }
        val ivParameterSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        val decryptedByteValue = cipher.doFinal()
        return String(decryptedByteValue)
    }

}


