package com.fabiolopz.security_essencials

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    //region statement
    private lateinit var fieldUser: EditText
    private lateinit var fieldPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonCreateAccount: Button
    //FireBase
    private lateinit var auth: FirebaseAuth
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
        //val currentUser = auth.currentUser
        // updateUI(currentUser)
    }

    private fun singIn(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
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
        fieldUser = findViewById(R.id.editTextUserL)
        fieldPassword = findViewById(R.id.editTextPasswordL)
        buttonLogin= findViewById(R.id.buttonLoginL)
        buttonCreateAccount = findViewById(R.id.buttonCreateAccountL)
        auth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.buttonLoginL-> showHomeActivity()
            R.id.buttonCreateAccountL -> showCreateAccountActivity()
        }
    }

    companion object {
        const val TAG = "Message_L_Activity"
    }
}
