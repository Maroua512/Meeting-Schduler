package com.meetingscheduler.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.MainActivity
import com.meetingscheduler.R

class Authentification : AppCompatActivity() {
    private val auth = Firebase.auth
    private lateinit var email :TextInputLayout
    private lateinit var password:TextInputLayout
    private lateinit var singUp : TextView
    private lateinit var btnLogin:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentification)
        //Intialisation des variables
        email = findViewById(R.id.Email)
        password = findViewById(R.id.password)
        singUp = findViewById(R.id.SignUpLink)
        btnLogin = findViewById(R.id.btnLogIn)
        email.editText?.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
             if(!isValidEmail(s.toString())){
              email.error = "Invalid email format"
             }
              else{
                  email.error  = null
             }
            }
        })
        //Click btn Login
        btnLogin.setOnClickListener {
            email.isErrorEnabled = false
            password.isErrorEnabled = false
            val emailText = email.editText?.text.toString()
            val passwordText = password.editText?.text.toString()
            if(emailText.isEmpty() || passwordText.isEmpty()){
                if(emailText.isEmpty()){
                    email.error = "Email is empty"
                    email.isErrorEnabled = true
                }
                if(passwordText.isEmpty() ){
                    password.error ="Password is empty"
                    password.isErrorEnabled = true
                }
            }
            else{
                Login(emailText,passwordText)
            }
        }
        singUp.setOnClickListener {
            Intent(this, Register::class.java).also {
                startActivity(it)
            }
        }

    }
    private  fun Login(emailText :String,passwordText:String){
        auth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener {result->
          if(result.isSuccessful){
           Intent(this, MainActivity::class.java).also {
               startActivity(it)
           }
          }
          else {
                  password.error = "Authentication failed!"
                 email.error = " "
                  password.isErrorEnabled = true
                  email.isErrorEnabled = true
          }
        }



    }
    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

}


