package com.eldroid.jobseeker.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eldroid.jobseeker.data.Job
import com.google.firebase.firestore.FirebaseFirestore

class BookmarkedJobsViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val bookmarkedJobs: MutableLiveData<List<Job>> = MutableLiveData()

    init {
        // Fetch bookmarked jobs from Firestore
        fetchBookmarkedJobs()
    }

    fun getBookmarkedJobs(): LiveData<List<Job>> {
        return bookmarkedJobs
    }

    fun onBookmarkClick(job: Job) {
        // Update the bookmark status in Firestore
        val updatedBookmarkStatus = !job.isBookmarked

        firestore.collection("jobs")
            .document(job.id) // Assuming "id" is the unique identifier for each job
            .update("isBookmarked", updatedBookmarkStatus)
            .addOnSuccessListener {
                // If the Firestore update is successful, update the local list and notify observers
                val updatedList = bookmarkedJobs.value.orEmpty().toMutableList()
                val updatedJob = job.copy(isBookmarked = updatedBookmarkStatus)
                updatedList[updatedList.indexOf(job)] = updatedJob
                bookmarkedJobs.value = updatedList
            }
            .addOnFailureListener {
                Log.d("BookmarkedJobsViewModel","Firestore Error")
            }
    }

    private fun fetchBookmarkedJobs() {
        // Replace "jobs" with your Firestore collection name
        firestore.collection("jobs")
            .whereEqualTo("isBookmarked", true) // Fetch only bookmarked jobs
            .get()
            .addOnSuccessListener { documents ->
                val bookmarkedJobList = mutableListOf<Job>()
                for (document in documents) {
                    val job = document.toObject(Job::class.java)
                    bookmarkedJobList.add(job)
                }
                bookmarkedJobs.value = bookmarkedJobList
            }
            .addOnFailureListener {
                Log.d("BookmarkedJobsViewModel","Firestore Error")
            }
    }
}