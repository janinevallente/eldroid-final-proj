package com.eldroid.jobseeker

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.eldroid.jobseeker.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.loginButton.setOnClickListener {
            val emailAddress: String = binding.emailEditText.text.toString().trim()
            val password: String = binding.passwordEditText.text.toString().trim()

            if (emailAddress.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                if (password.isNotEmpty()) {
                    firestore.collection("users")
                        .whereEqualTo("emailAddress", emailAddress)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            if (!querySnapshot.isEmpty) {
                                auth.signInWithEmailAndPassword(emailAddress, password)
                                    .addOnCompleteListener(this) { authTask ->
                                        if (authTask.isSuccessful) {
                                            showToast("Login Successful.")
                                            startNewActivity(Dashboard::class.java)
                                        } else {
                                            showToast("Login Failed.")
                                        }
                                    }
                            } else {
                                showToast("User not found.")
                            }
                        }
                        .addOnFailureListener { e ->
                            showToast("Error checking user: ${e.message}")
                        }
                } else {
                    binding.passwordEditText.error = "Please enter your password"
                }
            } else if (emailAddress.isEmpty()) {
                binding.emailEditText.error = "Please enter your email address."
            } else {
                binding.emailEditText.error = "Please enter a valid email address."
            }
        }

        binding.signupLink.setOnClickListener {
            startNewActivity(CreateAccount::class.java)
        }

        binding.forgotPassLink.setOnClickListener {
            startNewActivity(ForgotPassword::class.java)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmation()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startNewActivity(activityClass: Class<*>) {
        val enterAnimation = R.anim.slide_in_right_anim
        val exitAnimation = R.anim.slide_out_left_anim
        val options = ActivityOptions.makeCustomAnimation(this, enterAnimation, exitAnimation)
        val intent = Intent(this, activityClass)
        startActivity(intent, options.toBundle())
        finish()
    }

    private fun showExitConfirmation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Are you sure you want to exit?")
        builder.setPositiveButton("Yes") { _, _ ->
            finishAffinity()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}