package com.androidrider.login_signup_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.androidrider.login_signup_firebase.databinding.ActivityRegisterBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.btnRegister.setOnClickListener {
            val UserName = binding.edtUsername.text.toString()
            val Password = binding.edtPassword.text.toString()

            if (UserName.isNotEmpty() && Password.isNotEmpty()){
                signUpUser(UserName,Password)
            }else{
                Toast.makeText(this@RegisterActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    fun signUpUser(username: String, password: String){
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()){
                    val id = databaseReference.push().key
                    val userdata = DataClass(id, username, password)
                    databaseReference.child(id!!).setValue(userdata)

                    Toast.makeText(this@RegisterActivity, "Signup Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this@RegisterActivity, "User Already exist", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterActivity, "Database Error: ${error}", Toast.LENGTH_SHORT).show()
            }

        })

    }
}