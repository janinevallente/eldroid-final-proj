package com.eldroid.jobseeker

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eldroid.jobseeker.adapters.BookmarkedJobsAdapter
import com.eldroid.jobseeker.databinding.ActivityBookmarkedJobsBinding
import com.eldroid.jobseeker.models.BookmarkedJobsViewModel


class BookmarkedJobs : AppCompatActivity() {

    private lateinit var binding: ActivityBookmarkedJobsBinding
    private lateinit var bookmarkedJobsAdapter: BookmarkedJobsAdapter
    private lateinit var bookmarkedJobsViewModel: BookmarkedJobsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkedJobsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Initialize ViewModel
        bookmarkedJobsViewModel = ViewModelProvider(this)[BookmarkedJobsViewModel::class.java]

        // Initialize RecyclerView and Adapter
        bookmarkedJobsAdapter = BookmarkedJobsAdapter { clickedJob ->
            // Handle bookmark click here, e.g., update the bookmark status in the ViewModel
            bookmarkedJobsViewModel.onBookmarkClick(clickedJob)
        }

        binding.recyclerViewBookmarkedJobs.apply {
            layoutManager = LinearLayoutManager(this@BookmarkedJobs)
            adapter = bookmarkedJobsAdapter
        }

        // Observe changes in bookmarked jobs from ViewModel
        bookmarkedJobsViewModel.getBookmarkedJobs().observe(this) { bookmarkedJobs ->
            bookmarkedJobsAdapter.submitList(bookmarkedJobs)
        }
    }
}