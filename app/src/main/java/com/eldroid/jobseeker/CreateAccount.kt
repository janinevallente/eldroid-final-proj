package com.eldroid.jobseeker

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.eldroid.jobseeker.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateAccount : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.signUpButton.setOnClickListener {
            val fullName: String = binding.fullNameEditText.text.toString().trim()
            val emailAddress: String = binding.emailEditText.text.toString().trim()
            val password: String = binding.passwordEditText.text.toString().trim()

            if (fullName.isEmpty() || emailAddress.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@CreateAccount, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                binding.emailEditText.error = "Please enter a valid email address."
            }else{
                auth.createUserWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val uid = user?.uid

                            if (uid != null) {
                                // Create a user document in Firestore
                                val UserMap = hashMapOf(
                                    "accountId" to uid,
                                    "fullName" to fullName,
                                    "emailAddress" to emailAddress,
                                )

                                firestore.collection("users")
                                    .document(emailAddress)
                                    .set(UserMap)
                                    .addOnSuccessListener {
                                        binding.fullNameEditText.text!!.clear()
                                        binding.emailEditText.text!!.clear()
                                        binding.passwordEditText.text!!.clear()

                                        Toast.makeText(this@CreateAccount, "User registered successfully.", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this@CreateAccount, "User registration failed.", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this@CreateAccount, "Registration Failed: ${task.exception?.message ?: "Unknown error"}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        binding.signInLink.setOnClickListener {
            navigateWithAnimation(Login::class.java, R.anim.slide_in_left_anim, R.anim.slide_out_right_anim)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun navigateWithAnimation(activityClass: Class<*>, enterAnim: Int, exitAnim: Int) {
        val options = ActivityOptions.makeCustomAnimation(this, enterAnim, exitAnim)
        val intent = Intent(this, activityClass)
        startActivity(intent, options.toBundle())
        finish()
    }
}