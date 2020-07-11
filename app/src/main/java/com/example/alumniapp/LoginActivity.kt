package com.example.alumniapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth;

class LoginActivity : AppCompatActivity() {

    lateinit var loginButton:Button
    lateinit var loginEmail:EditText
    lateinit var loginPassword:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginButton=findViewById(com.example.alumniapp.R.id.button_login)
        loginEmail=findViewById(R.id.email_login)
        loginPassword=findViewById(R.id.password_login)
        loginButton.setOnClickListener {
            performLogin()
        }
    }
    private fun performLogin() {
        val email:String=loginEmail.text.toString()
        val password:String=loginPassword.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("LoginActivity", "Email is $email")
        Log.d("LoginActivity", "Password is $password")
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d("LoginActivity", "Login successful!")
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("LoginActivity", "Login failed")
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
            }
//        Log.d("a", "a")
    }
}