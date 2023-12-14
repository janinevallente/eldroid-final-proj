package com.eldroid.jobseeker

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.eldroid.jobseeker.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.pageText.text = "Home" //default name of the page upon launching

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null //sets app title as null to remove app name

        binding.navView.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.bottomNavigator.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_fragment -> {
                    replaceFragment(HomeFragment())
                    binding.pageText.text = "Home"
                }
                R.id.jobs_fragment -> {
                    replaceFragment(JobsFragment())
                    binding.pageText.text = "Jobs"
                }
                R.id.add_fragment -> {
                    replaceFragment(AddDiscussionFragment())
                    binding.pageText.text = "Create Discussion"
                }
                R.id.discussions_fragment -> {
                    replaceFragment(DiscussionsFragment())
                    binding.pageText.text = "Discussions"
                }
                R.id.about_us_fragment -> {
                    replaceFragment(AboutUsFragment())
                    binding.pageText.text = "About Us"
                }
                else -> {
                }
            }
            true
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            onNavigationItemSelected(menuItem)
            true
        }

        updateUserProfileInNavHeader()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        if (itemId == R.id.nav_bookmarked) {
            startActivity(Intent(applicationContext, BookmarkedJobs::class.java))
            return true
        } else if (itemId == R.id.nav_logout) {
            logoutConfirmation()
        }
        return false
    }

    private fun updateUserProfileInNavHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val fullNameView: TextView = headerView.findViewById(R.id.fullNameView)
        val emailView: TextView = headerView.findViewById(R.id.emailView)
        val currentUserEmail = auth.currentUser?.email

        if (currentUserEmail != null) {
            firestore.collection("users")
                .document(currentUserEmail)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val fullName = documentSnapshot.getString("fullName")
                        val email = documentSnapshot.getString("emailAddress")

                        fullNameView.text = fullName
                        emailView.text = email
                    }
                }
                .addOnFailureListener { e ->
                    showToast("Error fetching user data: ${e.message}")
                }
        } else {
            Log.d("Home Fragment", "User is null.")
        }
    }

    private fun logoutConfirmation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout Session")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { dialog: DialogInterface?, which: Int ->
            auth.signOut()
            val intent = Intent(applicationContext, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        //to avoid closing the application on back press
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}