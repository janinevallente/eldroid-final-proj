package com.eldroid.jobseeker.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eldroid.jobseeker.data.Job
import com.google.firebase.firestore.FirebaseFirestore

class JobViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val jobs: MutableLiveData<List<Job>> = MutableLiveData()

    init {
        // Fetch jobs from Firestore
        fetchJobs()
    }

    fun onBookmarkClick(job: Job) {
        // Update the bookmark status in Firestore
        val updatedBookmarkStatus = !job.isBookmarked // Toggle the bookmark icon status

        firestore.collection("jobs")
            .document(job.id) // Assuming "id" is the unique identifier for each job
            .update("isBookmarked", true)
            .addOnSuccessListener {
                // If the Firestore update is successful, update the local list and notify observers
                val updatedList = jobs.value.orEmpty().toMutableList()
                val updatedJob = job.copy(isBookmarked = updatedBookmarkStatus)
                updatedList[updatedList.indexOf(job)] = updatedJob
                jobs.value = updatedList
            }
            .addOnFailureListener {
                Log.d("JobViewModel","Firestore Error")
            }
    }


    fun getJobs(): LiveData<List<Job>> {
        return jobs
    }

    private fun fetchJobs() {
        // Replace "jobs" with your Firestore collection name
        firestore.collection("jobs")
            .whereEqualTo("isBookmarked", false)
            .get()
            .addOnSuccessListener { documents ->
                val jobList = mutableListOf<Job>()
                for (document in documents) {
                    val job = document.toObject(Job::class.java)
                    jobList.add(job)
                }
                jobs.value = jobList
            }
            .addOnFailureListener {
                Log.d("JobViewModel","Firestore Error")
            }
    }

}