package com.eldroid.jobseeker

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var topAnimation: Animation
    private lateinit var applicationLogo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main)

        //Animations
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)

        applicationLogo = findViewById(R.id.logoImageView)
        applicationLogo.startAnimation(topAnimation)

        val auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.currentUser != null) {
                // User is authenticated, navigate to Login
                navigateWithAnimation(Dashboard::class.java, R.anim.slide_in_right_anim, R.anim.slide_out_left_anim)
            } else {
                navigateWithAnimation(Login::class.java, R.anim.slide_in_right_anim, R.anim.slide_out_left_anim)
            }
        }, SPLASH_SCREEN.toLong())
    }

    private fun navigateWithAnimation(activityClass: Class<*>, enterAnim: Int, exitAnim: Int) {
        val options = ActivityOptions.makeCustomAnimation(this, enterAnim, exitAnim)
        val intent = Intent(this, activityClass)
        startActivity(intent, options.toBundle())
        finish()
    }

    companion object {
        private const val SPLASH_SCREEN = 3000
    }
}