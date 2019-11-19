package com.fabiolopz.security_essencials

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class CreateAccountActivity : AppCompatActivity(), View.OnClickListener {

    //region statement
    private lateinit var fieldEmail: EditText
    private lateinit var fieldPassword: EditText
    private lateinit var fieldConfirmPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonCreateAccount: Button

    //Encrypt
    private val keySecret:String = "fabio andres lopez perez"
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
    }

    private fun validateForm(): Boolean {
        var valid = true
        val email:String = fieldEmail.text.toString()
        val password:String = fieldPassword.text.toString()
        val confirmPassword:String = fieldConfirmPassword.text.toString()

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
            utilInputPasswordCA.error = null
        }
        if (confirmPassword.isEmpty()) {
            utilInputConfirmPassCA.error = "Required."
            valid = false
        } else {
            utilInputConfirmPassCA.error = null
        }
        if(validatePassword(password, confirmPassword)){
            Toast.makeText(baseContext, "Incorrect password.",
                Toast.LENGTH_SHORT).show()
            valid = false
        }
        return valid
    }

    private fun validatePassword(password:String, confirmPassword:String): Boolean =
        (password == "") || (confirmPassword == "")

    private fun createAccount(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Log.d(TAG, "usuario: ${user!!.email} success")
                    // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    // updateUI(null)
                }
            }
    }

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
        var cipher: Cipher? = null
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secret)
        return String(cipher.doFinal(cipherText), charset("UTF-8"))
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
        fieldEmail = findViewById(R.id.editTextEmailCA)
        fieldPassword = findViewById(R.id.editTextPasswordCA)
        fieldConfirmPassword = findViewById(R.id.editTextConfirmPasswordCA)
        buttonLogin = findViewById(R.id.buttonLoginCA)
        buttonCreateAccount = findViewById(R.id.buttonCreateAccountCA)
        auth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.buttonLoginCA -> showLoginActivity()
            R.id.buttonCreateAccountCA -> {
                if(validateForm()){
                    val keyGenerate = generateKey()
                    val resultEncrypt = encryptMsg(fieldPassword.text.toString(), keyGenerate)
                    val resultDecryptMsg = decryptMsg(resultEncrypt, keyGenerate)
                    createAccount(fieldEmail.text.toString(), resultDecryptMsg!!)

                    //Log.w(TAG, "El resultadoEncrypt es $resultEncrypt")
                    //Log.w(TAG, "El resultadoDEEEEESEncrypt es $resultDecryptMsg")
                    createAccount(fieldEmail.text.toString(), fieldPassword.text.toString())
                }
            }
        }
    }

    companion object {
        const val TAG = "Message_CA_Activity"
    }
}
