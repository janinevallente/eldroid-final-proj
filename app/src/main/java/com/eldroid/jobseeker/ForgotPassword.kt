package com.eldroid.jobseeker

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.eldroid.jobseeker.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ForgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.resetPasswordButton.setOnClickListener {
            val confirmEmail: String = binding.confirmEmailEditText.text.toString().trim()

            if (confirmEmail.isNotEmpty()) {
                checkUserExistence(confirmEmail)
            } else {
                showToast("Please enter your email address.")
            }
        }

        binding.backButton.setOnClickListener {
            navigateWithAnimation(Login::class.java, R.anim.slide_in_left_anim, R.anim.slide_out_right_anim)
        }
    }

    private fun checkUserExistence(confirmEmail: String) {
        firestore.collection("users")
            .whereEqualTo("emailAddress", confirmEmail)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    auth.sendPasswordResetEmail(confirmEmail)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                showToast("Password reset email sent to $confirmEmail")
                                navigateWithAnimation(Login::class.java, R.anim.slide_in_left_anim, R.anim.slide_out_right_anim)
                            } else {
                                showToast("Failed to send password reset email. Please check your email and try again.")
                            }
                        }
                } else {
                    showToast("User with this email does not exist.")
                }
            }
            .addOnFailureListener {
                showToast("Error checking user existence. Please try again.")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateWithAnimation(activityClass: Class<*>, enterAnim: Int, exitAnim: Int) {
        val options = ActivityOptions.makeCustomAnimation(this, enterAnim, exitAnim)
        val intent = Intent(this, activityClass)
        startActivity(intent, options.toBundle())
        finish()
    }
}