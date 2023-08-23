package com.androidrider.login_signup_firebase

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.androidrider.login_signup_firebase.databinding.ActivityLoginBinding
import com.androidrider.login_signup_firebase.databinding.ActivityRegisterBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)

        // Check if the user is already logged in
        if (isLoggedIn()) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
            return
        }

        binding.btnLogin.setOnClickListener {
            val UserName = binding.edtUsername.text.toString()
            val Password = binding.edtPassword.text.toString()

            if (UserName.isNotEmpty() && Password.isNotEmpty()){
                loginUser(UserName,Password)
            }else{
                Toast.makeText(this@LoginActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    fun loginUser(username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userData = userSnapshot.getValue(DataClass::class.java)

                            if (userData != null && userData.password == password) {
                                saveLoginStatus(true)
                                Toast.makeText(this@LoginActivity,"Login Successfully",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                                return
                            }
                        }
                    }
                    Toast.makeText(this@LoginActivity, "Invalid username or password!", Toast.LENGTH_SHORT).show()
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LoginActivity,"Database Error: ${error}",Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }
}